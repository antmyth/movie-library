package com.apn.filerenamer.persistence;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

import java.net.UnknownHostException;

public class MorphiaDb {

    private static Mongo mongo;
    private static Morphia morphia;

    public MorphiaDb() throws UnknownHostException {
        morphia = new Morphia();
        mongo = new Mongo();
    }

    private void map(Morphia morphia) {
//        morphia.map(Hotel.class).map(Address.class);
    }

    public Datastore getDatastore(String dbName) {
        return morphia.createDatastore(mongo, dbName);
    }
}