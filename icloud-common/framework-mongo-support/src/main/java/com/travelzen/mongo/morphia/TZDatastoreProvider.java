package com.travelzen.mongo.morphia;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.mapping.lazy.DatastoreProvider;

public class TZDatastoreProvider implements DatastoreProvider {

    private static final long serialVersionUID = -1626052211006333141L;

    private Datastore datastore;

    @Override
    public Datastore get() {
        return datastore;
    }

    public void set(Datastore ds) {
        this.datastore = ds;
    }

}
