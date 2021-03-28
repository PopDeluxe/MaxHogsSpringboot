package com.popdeluxe.maxhogs.hogs;

import java.util.List;

public class CurrentRiverRace {
    public String state;
    public Clan clan;
    public List<Clan> clans;
    public String collectionEndTime;
    public String warEndTime;
    public int sectionIndex;


    public class Participant {
        public String tag;
        public String name;
        public int fame;
        public int repairPoints;
        public int boatAttacks;
        public int decksUsed;
    }

    public class Clan {
        public String tag;
        public int clanScore;
        public int badgeId;
        public String name;
        public int fame;
        public int repairPoints;
        public String finishTime;
        public List<Participant> participants;
    }



}




