class NumberDemo {

    public static void main(String[] args) {

        NumberPrinter oddPrinter = new NumberPrinter("odd");

        NumberPrinter evenPrinter = new NumberPrinter("even");

        oddPrinter.start();

        evenPrinter.start();

        System.out.println("Calling thread Done");

    }

}


