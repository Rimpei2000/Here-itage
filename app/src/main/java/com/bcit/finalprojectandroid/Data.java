package com.bcit.finalprojectandroid;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Data{
    public class Parameters implements Serializable{
        public String dataset;
        public String timezone;
        public int rows;
        public int start;
        public String format;
        public List<String> facet;
    }

    public class Geom implements Serializable{
        public String type;
        public List<Double> coordinates;
    }

    public static class Fields implements Serializable{
        public String status;
        public String category;
        public String streetnumber;
        public String provincialdesignationp;
        public List<Double> geo_point_2d;
        public String evaluationgroup;
        public String buildingnamespecifics;
        public String interiordesignationi;
        public String localarea;
        public String heritageconservationareaca;
        public String heritagerevitalizationagreementh;
        public String id;
        public Date dateoflastupdate;
        public String municipaldesignationm;
        public Geom geom;
        public String heritageconservationcovenanthc;
        public String landscapedesignationl;
        public String federaldesignationf;
        public String additionallocationinformation;
        public String streetname;

        public Fields(String buildingnamespecifics) {
            this.buildingnamespecifics = buildingnamespecifics;
        }
    }

    public class Geometry implements Serializable{
        public String type;
        public List<Double> coordinates;
    }

    public static class Record implements Serializable {
        public String datasetid;
        public String recordid;
        public Fields fields;
        public Geometry geometry;
        public Date record_timestamp;

        public Record(String datasetid, String recordid, Fields fields, Geometry geometry, Date record_timestamp) {
            this.datasetid =  datasetid;
            this.recordid = recordid;
            this.fields = fields;
            this.geometry = geometry;
            this.record_timestamp = record_timestamp;
        }
    }

    public class Facet implements Serializable{
        public int count;
        public String path;
        public String state;
        public String name;
    }

    public class FacetGroup implements Serializable{
        public List<Facet> facets;
        public String name;
    }

    public class Root implements Serializable{
        public int nhits;
        public Parameters parameters;
        public List<Record> records;
        public List<FacetGroup> facet_groups;
    }
}

