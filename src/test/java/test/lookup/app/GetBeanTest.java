package test.lookup.app;

import test.lookup.bean.User;

public abstract class GetBeanTest {
    public void showMe(){
        this.getBean().showMe();
    }

    protected abstract User getBean();
}
