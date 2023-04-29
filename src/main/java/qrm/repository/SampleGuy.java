package qrm.repository;

import java.util.Collection;

public class SampleGuy extends BaseGuy {

    public SampleGuy() {
        super();
    }

    @Query(text = "text", dataType = SomeData.class)
    public Collection<SomeData> fetch(){
        System.out.println("hello");
        return null;
    }
}
