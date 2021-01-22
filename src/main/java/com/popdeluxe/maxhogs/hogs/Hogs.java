package com.popdeluxe.maxhogs.hogs;

import java.util.List;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */

public class Hogs {
    public List<Item> items;
    public Paging paging;


    public class Arena {
        public int id;
        public String name;
    }

    public class Item {
        public String tag;
        public String name;
        public String role;
        public String lastSeen;
        public int expLevel;
        public int trophies;
        public Arena arena;
        public int clanRank;
        public int previousClanRank;
        public int donations;
        public int donationsReceived;
        public int clanChestPoints;
    }

    public class Cursors {
    }

    public class Paging {
        public Cursors cursors;
    }

    @Override
    public String toString() {
        return "Hogs{" +
                "items=" + items +
                ", paging=" + paging +
                '}';
    }
}



