package org.kevoree.modeling.addons.rest;

import org.junit.Test;
import org.kevoree.modeling.KCallback;
import org.kevoree.modeling.KModel;
import org.kevoree.modeling.KObject;
import org.kevoree.modeling.memory.manager.DataManagerBuilder;
import org.kevoree.modeling.meta.KMetaClass;
import org.kevoree.modeling.meta.KPrimitiveTypes;
import org.kevoree.modeling.meta.impl.MetaModel;

public class DemoTest {

    //@Test
    public void test() {
        KModel cloudModel = buildMiniModel();
        cloudModel.connect(new KCallback() {
            @Override
            public void on(Object o) {
                for (int i = 0; i < 10; i++) {
                    KObject nodeLoop = cloudModel.createByName("Node", 0, i);
                    nodeLoop.setByName("name", "node" + i);
                    nodeLoop.setByName("load", i);

                    KObject subProcessLoop = cloudModel.createByName("Process", 0, i);
                    subProcessLoop.setByName("name", "process" + i);
                    subProcessLoop.setByName("load", i);
                    nodeLoop.addByName("processes", subProcessLoop);
                }
                RestGateway gateway = RestGateway.expose(cloudModel, 8050);
                gateway.start();
            }
        });
    }

    private KModel buildMiniModel() {
        MetaModel mm = new MetaModel("Cloud");
        KMetaClass node_class = mm.addMetaClass("Node");
        node_class.addAttribute("name", KPrimitiveTypes.STRING).setKey(true);
        node_class.addAttribute("load", KPrimitiveTypes.DOUBLE);

        KMetaClass process_class = mm.addMetaClass("Process");
        process_class.addAttribute("name", KPrimitiveTypes.STRING);
        process_class.addAttribute("load", KPrimitiveTypes.DOUBLE);

        node_class.addRelation("processes", process_class, null);

        return mm.createModel(DataManagerBuilder.buildDefault());
    }

    public static void main(String[] args) {
        DemoTest test = new DemoTest();
        test.test();
    }

}
