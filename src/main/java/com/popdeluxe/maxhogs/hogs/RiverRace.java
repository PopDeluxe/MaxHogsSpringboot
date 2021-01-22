package com.popdeluxe.maxhogs.hogs;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */

import java.util.List;

public class RiverRace {
    public List<Item> items;
    public Paging paging;

    public class Participant {
        public String tag;
        public String name;
        public int fame;
        public int repairPoints;
        public int boatAttacks;
    }

    public class Clan {
        public String tag;
        public String name;
        public int badgeId;
        public int fame;
        public int repairPoints;
        public String finishTime;
        public List<Participant> participants;
        public int clanScore;
    }

    public class Standing {
        public int rank;
        public int trophyChange;
        public Clan clan;
    }

    public class Item {
        public int seasonId;
        public int sectionIndex;
        public String createdDate;
        public List<Standing> standings;
    }

    public class Cursors {
    }

    public class Paging {
        public Cursors cursors;
    }
}



