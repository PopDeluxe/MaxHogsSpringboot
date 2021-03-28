package com.popdeluxe.maxhogs.hogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hog implements Comparable<Hog> {

    private String tag;
    private String name;
    private String role;
    //private Date lastSeen;
    private int expLevel;
    private int trophies;
    private int clanRank;
    private int previousClanRank;
    private int donations;
    private int donationsReceived;
    private int clanChestPoints;
    private int fame;
    private int repairs;
    private int boatAttacks;
    private int donationScore;
    private int riverScore;
    private int currentRiverAttacks;
    private int hogScore;
    private BigDecimal unroundedHogScore;


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getExpLevel() {
        return expLevel;
    }

    public void setExpLevel(int expLevel) {
        this.expLevel = expLevel;
    }

    public int getTrophies() {
        return trophies;
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    public int getClanRank() {
        return clanRank;
    }

    public void setClanRank(int clanRank) {
        this.clanRank = clanRank;
    }

    public int getPreviousClanRank() {
        return previousClanRank;
    }

    public void setPreviousClanRank(int previousClanRank) {
        this.previousClanRank = previousClanRank;
    }

    public int getDonations() {
        return donations;
    }

    public void setDonations(int donations) {
        this.donations = donations;
    }

    public int getDonationsReceived() {
        return donationsReceived;
    }

    public void setDonationsReceived(int donationsReceived) {
        this.donationsReceived = donationsReceived;
    }

    public int getClanChestPoints() {
        return clanChestPoints;
    }

    public void setClanChestPoints(int clanChestPoints) {
        this.clanChestPoints = clanChestPoints;
    }

    public int getFame() {
        return fame;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    public int getRepairs() {
        return repairs;
    }

    public void setRepairs(int repairs) {
        this.repairs = repairs;
    }

    public int getBoatAttacks() {
        return boatAttacks;
    }

    public void setBoatAttacks(int boatAttacks) {
        this.boatAttacks = boatAttacks;
    }

    public int getHogScore() {
        return hogScore;
    }

    public void setHogScore(int hogScore) {
        this.hogScore = hogScore;
    }

    public int getDonationScore() {
        return donationScore;
    }

    public void setDonationScore(int donationScore) {
        this.donationScore = donationScore;
    }

    public int getRiverScore() {
        return riverScore;
    }

    public void setRiverScore(int riverScore) {
        this.riverScore = riverScore;
    }

    public BigDecimal getUnroundedHogScore() {
        return unroundedHogScore;
    }

    public void setUnroundedHogScore(BigDecimal unroundedHogScore) {
        this.unroundedHogScore = unroundedHogScore;
    }

    public int getCurrentRiverAttacks() {
        return currentRiverAttacks;
    }

    public void setCurrentRiverAttacks(int currentRiverAttacks) {
        this.currentRiverAttacks = currentRiverAttacks;
    }

    @Override
    public int compareTo(Hog o) {

        /*
        if(this.getUnroundedHogScore() == null || o.getUnroundedHogScore() == null){
            return 0;
        }
        else {
            return o.getUnroundedHogScore().compareTo(this.getUnroundedHogScore());
        }
        
         */

        if (o.getCurrentRiverAttacks() > this.getCurrentRiverAttacks()) {
            return 1;
        }
        else if (o.getCurrentRiverAttacks() < this.getCurrentRiverAttacks()) {
            return -1;
        }
        else {
            return 0;
        }


    }
}
