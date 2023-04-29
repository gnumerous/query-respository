package qrm.repository;

import java.util.stream.Stream;

abstract public class BaseGuy {

    private static final String myAnnot = Query.class.getName();

    public BaseGuy() {
        System.out.println("class level annotations");
        Stream.of(this.getClass().getAnnotations()).forEach(annot -> {
            if (annot.annotationType().getName().equals(myAnnot)) {
                System.out.println(annot);
            }
        });
        System.out.println("\nmethod level annotations");
        Stream.of(this.getClass().getMethods()).forEach(mtd ->
                Stream.of(mtd.getAnnotations()).forEach(annot -> {
                    if (annot.annotationType().getName().equals(myAnnot)) {
                        System.out.println(annot);
                    }
                })

        );
    }
}
