package com.travelzen;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.omg.CORBA_2_3.portable.OutputStream;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.UpdateOperations;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.travelzen.mongo.dao.FooDao;
import com.travelzen.mongo.entity.Foo;
import com.travelzen.mongo.entity.User;
import com.travelzen.mongo.morphia.JodaTimeConverter;

public class AppTest {

    public static void main(String[] args) throws Exception {
        Mongo mng = new MongoClient("127.0.0.1");
        Datastore ds = new Morphia().createDatastore(mng, "test", "test", "test".toCharArray());

//        MongoClientURI mcu = new MongoClientURI("mongodb.uri=mongodb://tzuser:asdf@192.168.160.78:27017,192.168.160.77:27017,192.168.160.76:27017/usercenter");
//        Mongo mng = new MongoClient(mcu);
//        Datastore ds = new Morphia().createDatastore(mng, "test");

        ds.getMapper().getConverters().addConverter(JodaTimeConverter.class);

        FooDao fd = new FooDao();
        fd.setDatastore(ds);

//        List<Foo> list = ds.createQuery(Foo.class).field("values").containsIgnoreCase("BB").asList();
//        System.out.println(list.iterator().next().getValues());

//        UpdateOperations<Foo> uo = ds.createUpdateOperations(Foo.class);
//        uo.removeAll("values", "ddd");
//        ds.update(ds.createQuery(Foo.class).field("name").equal("arraytest"), uo);

//        Foo f = new Foo();
//        f.setName("arraytest");
//        f.setGender("male");
//        f.setValues(Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee"));
//        fd.create(f);

//        User a = new User();
//        a.setName("embed test");
//        Foo f = new Foo();
//        f.setName("embededtestffff");
//        f.setGender("aaaa");
//        a.setFoo(f);
//        ds.save(a);

//        List<User> result = ds.find(User.class).asList();
//        User u  = result.get(0);
//        System.out.println(u.getId());
//        Foo fo = new Foo();
//        fo.setName("4gredf");
//        u.setFoo(fo);
//        ds.merge(u);

    }

}
