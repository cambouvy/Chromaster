package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ChromaticNumber {

    private static ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(6);

    public static boolean DEBUG_FLAG = true;

    //---
    private final static long TIME_LIMIT_EXACT = TimeUnit.SECONDS.toNanos(60);
    private final static long TIME_LIMIT_LOWER = TimeUnit.SECONDS.toNanos(10);
    private final static long TIME_LIMIT_UPPER = TimeUnit.SECONDS.toNanos(10);

    public enum Type {
        UPPER,
        LOWER,
        EXACT,
        EXACT_EXPERIMENTAL
    }

    public static void  computeAsync(Type type, Graph graph, Consumer<Result> consumer) {
        CompletableFuture.supplyAsync(() -> compute(type, graph, false), schedule).thenAccept(consumer);
    }


    public static Result compute(Type type, Graph graph, boolean runTimeBound) {
        graph.reset();

        switch (type) {

            case LOWER: return runTimeBound ? limitedTimeLowerBound(graph) : new Result(null,-1, lowerBound(graph), -1, true);
            case UPPER: return runTimeBound ? limitedTimeUpper(graph) : new Result(null,-1, -1, upperBound(graph), true);
            case EXACT: return runTimeBound ? limitedTimeExactTest(graph) : exactTest(graph, false);
            case EXACT_EXPERIMENTAL: return runTimeBound ? limitedTimeExactTest(graph) : exactParallelled(graph, false);

        }
        throw new IllegalStateException();
    }

    //---
    private static Result limitedTimeExactTest(Graph graph) {
        return timeBoundMethodExecution(new MethodRunnable() {
            @Override
            public void run() {
                this.setResult(exactParallelled(graph, true));
            }
        }, TIME_LIMIT_EXACT);
    }

    private static Result limitedTimeLowerBound(Graph graph) {
        Result result = timeBoundMethodExecution(new MethodRunnable() {
            @Override
            public void run() {
                Result r = new Result(null,-1, lowerBound(graph), -1, true);
                this.setResult(r);
            }
        }, TIME_LIMIT_LOWER);

        if(result.getLower() == -1) {
            result = new Result(null,-1, basicLowerBound(graph), -1, true);
        }

        return result;
    }

    private static int basicLowerBound(Graph graph) {
        int tmp = graph.getEdges().entrySet().stream().mapToInt(e -> e.getValue().size()).min().getAsInt();
        return (tmp == 1) ? 2 : tmp;
    }

    private static Result limitedTimeUpper(Graph graph) {
        Result result = timeBoundMethodExecution(new MethodRunnable() {
            @Override
            public void run() {
                Result r = new Result(null,-1, -1, upperBound(graph), true);
                this.setResult(r);
            }
        }, TIME_LIMIT_UPPER);

        if(result.getUpper() == -1) {
            result = new Result(null,0, 0, simpleUpperBound(graph), true);
        }
        return result;
    }

    // --- EXACT_EXPERIMENTAL SECTION ---
    private static Result exactTest(Graph graph, boolean runTimeBound) {
        //---
        final int upper = runTimeBound ? limitedTimeUpper(graph).getUpper() : upperBound(graph);
        final int lower = runTimeBound ? limitedTimeLowerBound(graph).getLower() : lowerBound(graph);
        System.out.printf("<Exact Test> Range: [%d..%d]%n", lower, upper);

        graph.reset();

        if(upper == lower) {
            exact(graph, upper);
            System.out.printf("<Exact Test> Exact: %d", lower);
            return new Result(graph, upper, upper, upper, true);
        }

        int testValue = upper - 1;
        Graph result = graph;
        while(exact(graph, testValue)) {
            System.out.printf("<Exact Test> The graph CAN be coloured with %d colours.%n", testValue);
            result = graph.clone();
            graph.reset();

            if(testValue == lower) {
                testValue--;
                break;
            }
            testValue--;

        }


        final int exact = testValue+1;
        System.out.printf("<Exact Test> Exact: %d", exact);
        return new Result(result, exact, lower, upper, true);
    }


    private static Result exactParallelled(Graph graph, boolean runTimeBound) {
        //--- the upper bound that we either find by running our upper-bound algorithm
        final AtomicInteger upper = new AtomicInteger(runTimeBound ? limitedTimeUpper(graph).getUpper() : upperBound(graph));

        // if the upper bound algorithm fails, we cannot do anything anymore
        if(upper.get() == -1) {
            return new Result(null,-1, -1, -1, true);
        }

        // run the lower bound algorithm, if it is supposed to be time-limited
        AtomicInteger lower = new AtomicInteger(0);
        if(runTimeBound) {
            lower.set(limitedTimeLowerBound(graph).getLower());
        }

        //--- the current range of values we are expecting to inspect
        final int upperResult = upper.get();
        final int lowerResult = lower.get();
        if(DEBUG_FLAG) System.out.printf("<Exact Test: %d> Range: [%d..%d]%n", graph.hashCode(), lowerResult, upperResult);

        //--- if the bounds are equal then this is the chromatic number
        if(upperResult == lowerResult) {
            return new Result(graph, lowerResult, lowerResult, upperResult, true);
        }

        //--- we do start testing upperBound-1 because we know for sure that upper-bound itself is going to work, so
        // testing it is a waste of resources.
        AtomicInteger testValue = new AtomicInteger(upper.get());
        testValue.addAndGet(-1);
        graph.reset();

        AtomicReference<Graph> colouredGraph = new AtomicReference<>();

        //--- Run the exact test async, so we can run the lower-bound algorithm in parallel
        final AtomicReference<Thread> exactFuture = new AtomicReference<>();
        final AtomicReference<Thread> lowerBoundFuture = new AtomicReference<>();
        final AtomicBoolean updateExact = new AtomicBoolean(true);

        exactFuture.set(new Thread(() -> {

            while (exactFuture.get() == null) {}

            while(!exactFuture.get().isInterrupted() && exact(graph, testValue.get())) {
                if(DEBUG_FLAG) System.out.printf("<Exact Test: %d> The graph CAN be coloured with %d colours.%n", graph.hashCode(), testValue.get());
                colouredGraph.set(graph.clone());
                graph.reset();

                if(testValue.get() == lower.get()) {
                    if (!exactFuture.get().isInterrupted()) {
                        testValue.addAndGet(-1);
                    }
                    break;
                }
                // TODO cleanup
                else if(testValue.get() < lower.get()) {
                    if(!exactFuture.get().isInterrupted()) {
                        testValue.set(lower.get() - 1);
                    }
                    break;
                }
                testValue.addAndGet(-1);
            }

            while (lowerBoundFuture.get() == null) {}
            if(!(exactFuture.get().isInterrupted())) {
                if (lowerBoundFuture.get() != null) {
                    lowerBoundFuture.get().interrupt();
                }
            }

        }));

        //--- run the lower-bound algorithm async at the same time as the exact tests are going on
        if(!(runTimeBound)) {
            lowerBoundFuture.set(new Thread(() -> {
                final int result = lowerBound(graph);

                lower.set(result);
                if(DEBUG_FLAG) System.out.printf("<Exact Test: %d> Updated lower bound: %d%n", graph.hashCode(), lower.get());
                if(DEBUG_FLAG) System.out.printf("<Exact Test: %d> Range: [%d..%d]%n", graph.hashCode(), lower.get(), upperResult);

                //--- if the result is greater the upper-bound
                // then we are done, and the result (lower-bound) is the chromatic number.
                if (result == upperResult) {
                    exactFuture.get().interrupt(); // cancel the main check to stop it from eroding our data.

                    while (lowerBoundFuture.get() == null) {}

                    if (!lowerBoundFuture.get().isInterrupted()) {
                        testValue.set(result - 1); // set result
                        if(DEBUG_FLAG) System.out.printf("<Exact Test: %d> Exact: %d (determined by lower-bound async execution)%n", graph.hashCode(), (testValue.get() + 1));
                    }
                }
            }));
        }

        //--- we have to wait for both the lower-bound (if running at all), and the exact test to finish before submitting
        // any results
        try {
            if(lowerBoundFuture.get() != null) {
                lowerBoundFuture.get().join();
            }

            exactFuture.get().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //--- we are done, we have to increase the upper-bound by +1 because it contains the current upper-bound we tested
        // was no longer valid so the value before that is the chromatic number.
        final int exact = testValue.get() + 1;
        if(DEBUG_FLAG) System.out.printf("<Exact Test: %d> Exact: %d%n", graph.hashCode(), exact);
        return new Result(colouredGraph.get(), exact, lower.get(), upper.get(), true);


    }

    private static boolean exact(Graph graph, int colours) {
        return exact(graph, colours, graph.getNode(graph.getMinNodeId()), 0, 0);
    }

    private static boolean exact(Graph graph, int color_nb, Node node, int level, int maxl) {
        maxl = Math.max(maxl, level);

        //--- Are all nodes coloured? If so, we are done.
        if(graph.getNodes().values().stream().noneMatch(e -> e.getValue() == -1)) {
            return true;
        }

        //--- Check this note for all colours
        for(int c = 1; c <= color_nb; c++) {
            if(exactIsColourAvailable(graph, node, c)) {
                node.setValue(c);

                Node next = graph.getNextAvailableNode(node);
                if(next == null || exact(graph, color_nb, next, level + 1, maxl)) {
                    return true;
                }

                node.setValue(-1);
            }
        }

        return false;
    }

    private static boolean exactIsColourAvailable(Graph graph, Node node, int colour) {
        return graph.getEdges(node.getId()).stream().noneMatch(e -> e.getTo().getValue() == colour);
    }

    // --- UPPER BOUND SECTION ---
    private static int upperBound(Graph graph) {
        return upperBoundIterative(graph);
    }

    private static int simpleUpperBound(Graph graph) {
        return graph.getEdges().values().stream().mapToInt(Map::size).max().getAsInt() + 1;
    }

    private static int upperBoundIterative(Graph graph) {
        //--- Build unvisited map ordered by degree of nodes descending
        Stack<Node> unvisited = graph.getNodes().values().stream()
                .sorted(Comparator.comparingInt(o -> graph.getEdges(o.getId()).size()))
                .collect(Collectors.toCollection(Stack::new));
        int max = 0;
        while (!unvisited.isEmpty()){
            Node node = unvisited.pop();

            //--- What colours does its neighbours have?
            List<Node.Edge> edges = graph.getEdges(node.getId());
            List<Integer> colours = edges.stream()
                    .filter(edge -> edge.getTo().getValue() != -1)
                    .map(edge -> edge.getTo().getValue())
                    .collect(Collectors.toList());

            //--- No colours -> first node being visited in the graph
            if (colours.isEmpty()) {
                node.setValue(0);
            }
            //--- At least one colour -> not the first node anymore
            else {

                //--- "Highest"  value/colour adjacent to the node
                final int maxColour = colours.stream().max(Comparator.naturalOrder()).get();

                int colour = 0; // Lowest value we can chose for a valid colour

                //--- try to ideally find an existing colour that we can reuse
                while (colour <= maxColour) {
                    if (!colours.contains(colour)) {
                        break;
                    }
                    colour++;
                }

                node.setValue(colour);
                max = Math.max(max, colour);

            }

        }

        return max + 1;

    }

    //--- LOWER BOUND --

    private static int lowerBound(Graph graph) {
        return bronKerbosch(graph, new ArrayList<>(), new ArrayList<>(graph.getNodes().values()), new ArrayList<>());
    }
    private static int bronKerbosch(Graph graph, List<Node> _R, List<Node> _P, List<Node> _X) {
        int max = Integer.MIN_VALUE;
        if(_P.isEmpty() && _X.isEmpty()) {
            max = Math.max(max, _R.size());
        }

        Iterator<Node> nodeIterator = _P.iterator();
        while (nodeIterator.hasNext()) {

            //---
            Node node = nodeIterator.next();
            List<Node> neighbours = graph.getEdges(node.getId()).stream().map(Node.Edge::getTo).collect(Collectors.toList());

            //---
            List<Node> dR = new ArrayList<>(_R);
            dR.add(node);

            List<Node> dP = _P.stream().filter(neighbours::contains).collect(Collectors.toList());
            List<Node> dX = _X.stream().filter(neighbours::contains).collect(Collectors.toList());

            max = Math.max(bronKerbosch(graph, dR, dP, dX), max);

            //---
            nodeIterator.remove();
            _X.add(node);
        }

        return max;
    }

    //--- Utility
    public static Result timeBoundMethodExecution(MethodRunnable runnable, final long timeInMilliseconds) {
        Thread thread = new Thread(runnable);
        thread.start();
        long time = System.nanoTime();
        long countdown = time + timeInMilliseconds;

        // TODO replace busy waiting
        while (!runnable.getResult().isReady() && time < countdown) {
            System.out.print(""); //for some reason this code does not work without this. is there maybe some sort of byte-code optimisation going on removing this type of loop
            time = System.nanoTime();
        }
        //thread.interrupt();

        return runnable.getResult();

    }

    private static abstract class MethodRunnable implements Runnable {

        private Result result = new Result(null, -1, -1, -1, false);

        @Override
        public abstract void run();

        public void setResult(Result result) {
            this.result = result;
        }

        public Result getResult() {
            return this.result;
        }

    }

    public static class Result {

        private Graph solution;

        private int exact = -1;
        private int upper = -1;
        private int lower = -1;

        private boolean isReady = false;

        public Result(Graph solution, int exact, int lower, int upper, boolean isReady) {
            this.solution = solution;
            this.exact = exact;
            this.lower = lower;
            this.upper = upper;
            this.isReady = isReady;
        }

        public Graph getSolution() {
            return solution;
        }

        public void ready() {
            this.isReady = true;
        }

        public boolean isReady() {
            return isReady;
        }

        public int getExact() {
            return exact;
        }

        public int getLower() {
            return lower;
        }

        public int getUpper() {
            return upper;
        }

    }

}
