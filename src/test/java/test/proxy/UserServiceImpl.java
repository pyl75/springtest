package test.proxy;

public class UserServiceImpl implements UserService{
    @Override
    public void add() {
        System.out.println("----------------------add--------------------");
    }

    @Override
    public void sayHello() {
        System.out.println("----------------------hello------------------");
    }
}
