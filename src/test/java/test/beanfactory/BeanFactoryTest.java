package test.beanfactory;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class BeanFactoryTest {
    @Test
    public void testSimpleLoad(){
        BeanFactory bf = new XmlBeanFactory(new ClassPathResource("test/beanFactory/beanFactoryTest.xml"));
        MyTestBean myTestBean = (MyTestBean) bf.getBean("myTestBean");
    }
}
