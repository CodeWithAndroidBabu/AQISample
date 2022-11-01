package co.android.helper.hkaqi.data.model;

public class Test {
    void test(){
        Demo d = new Demo(){
            @Override
            public void m2() {

            }
            @Override
            public void m3() {

            }
        };
        d.m1();

        DemoInterface d1 = new DemoInterface() {
            @Override
            public void m1() {


            }

            @Override
            public void m2() {

            }

            @Override
            public void m3() {

            }
        };
    }
}
