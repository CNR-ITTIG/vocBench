package org.fao.aoscs.domain;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.gilead.pojo.gwt.LightEntity;

public class StatisticalData extends LightEntity{

	private static final long serialVersionUID = -7685181800404495116L;

	private HashMap<String, Integer> countNumberOfUsersPerLanguage;
	
	private int countNumberOfUser;

	private ObjectPerUserStat countNumberOfTermCreatedByUser;
	private ObjectPerUserStat countNumberOfTermEditedByUser;
	private ObjectPerUserStat countNumberOfTermDeletedByUser;
	
	private ObjectPerUserStat countNumberOfConceptCreatedByUser;
	private ObjectPerUserStat countNumberOfConceptEditedByUser;
	private ObjectPerUserStat countNumberOfConceptDeletedByUser;
	
	private HashMap<String, Integer> countNumberOfTermPerStatus;
	
	private HashMap<String, Integer> countNumberOfConceptPerStatus;
	
	private HashMap<Integer, Integer> countNumberOfRelationshipsPerUsers;
	
	private HashMap<Integer, Integer> checkNumberOfConnectionPerUser;
	
	private String checkWhoLastConnected;
	
	private HashMap<Integer, String> checkNumberOfLastModificationPerUser;
	
	private HashMap<String, Integer> countNumberOfConceptPerRelationship;
	
	private HashMap<String, HashMap<String, ArrayList<ClassObject>>> listTheDomainAndRangeForRelationship;
	
	private HashMap<String, Integer> countNumberOfExports;

	public HashMap<String, Integer> getCountNumberOfUsersPerLanguage() {
		return countNumberOfUsersPerLanguage;
	}

	public void setCountNumberOfUsersPerLanguage(
			HashMap<String, Integer> countNumberOfUsersPerLanguage) {
		this.countNumberOfUsersPerLanguage = countNumberOfUsersPerLanguage;
	}

	public int getCountNumberOfUser() {
		return countNumberOfUser;
	}

	public void setCountNumberOfUser(int countNumberOfUser) {
		this.countNumberOfUser = countNumberOfUser;
	}

	public ObjectPerUserStat getCountNumberOfTermCreatedByUser() {
		return countNumberOfTermCreatedByUser;
	}

	public void setCountNumberOfTermCreatedByUser(
			ObjectPerUserStat countNumberOfTermCreatedByUser) {
		this.countNumberOfTermCreatedByUser = countNumberOfTermCreatedByUser;
	}

	public ObjectPerUserStat getCountNumberOfTermEditedByUser() {
		return countNumberOfTermEditedByUser;
	}

	public void setCountNumberOfTermEditedByUser(
			ObjectPerUserStat countNumberOfTermEditedByUser) {
		this.countNumberOfTermEditedByUser = countNumberOfTermEditedByUser;
	}

	public ObjectPerUserStat getCountNumberOfTermDeletedByUser() {
		return countNumberOfTermDeletedByUser;
	}

	public void setCountNumberOfTermDeletedByUser(
			ObjectPerUserStat countNumberOfTermDeletedByUser) {
		this.countNumberOfTermDeletedByUser = countNumberOfTermDeletedByUser;
	}

	public ObjectPerUserStat getCountNumberOfConceptCreatedByUser() {
		return countNumberOfConceptCreatedByUser;
	}

	public void setCountNumberOfConceptCreatedByUser(
			ObjectPerUserStat countNumberOfConceptCreatedByUser) {
		this.countNumberOfConceptCreatedByUser = countNumberOfConceptCreatedByUser;
	}

	public ObjectPerUserStat getCountNumberOfConceptEditedByUser() {
		return countNumberOfConceptEditedByUser;
	}

	public void setCountNumberOfConceptEditedByUser(
			ObjectPerUserStat countNumberOfConceptEditedByUser) {
		this.countNumberOfConceptEditedByUser = countNumberOfConceptEditedByUser;
	}

	public ObjectPerUserStat getCountNumberOfConceptDeletedByUser() {
		return countNumberOfConceptDeletedByUser;
	}

	public void setCountNumberOfConceptDeletedByUser(
			ObjectPerUserStat countNumberOfConceptDeletedByUser) {
		this.countNumberOfConceptDeletedByUser = countNumberOfConceptDeletedByUser;
	}

	public HashMap<String, Integer> getCountNumberOfTermPerStatus() {
		return countNumberOfTermPerStatus;
	}

	public void setCountNumberOfTermPerStatus(
			HashMap<String, Integer> countNumberOfTermPerStatus) {
		this.countNumberOfTermPerStatus = countNumberOfTermPerStatus;
	}

	public HashMap<String, Integer> getCountNumberOfConceptPerStatus() {
		return countNumberOfConceptPerStatus;
	}

	public void setCountNumberOfConceptPerStatus(
			HashMap<String, Integer> countNumberOfConceptPerStatus) {
		this.countNumberOfConceptPerStatus = countNumberOfConceptPerStatus;
	}

	public HashMap<Integer, Integer> getCountNumberOfRelationshipsPerUsers() {
		return countNumberOfRelationshipsPerUsers;
	}

	public void setCountNumberOfRelationshipsPerUsers(
			HashMap<Integer, Integer> countNumberOfRelationshipsPerUsers) {
		this.countNumberOfRelationshipsPerUsers = countNumberOfRelationshipsPerUsers;
	}

	public HashMap<Integer, Integer> getCheckNumberOfConnectionPerUser() {
		return checkNumberOfConnectionPerUser;
	}

	public void setCheckNumberOfConnectionPerUser(
			HashMap<Integer, Integer> checkNumberOfConnectionPerUser) {
		this.checkNumberOfConnectionPerUser = checkNumberOfConnectionPerUser;
	}

	public String getCheckWhoLastConnected() {
		return checkWhoLastConnected;
	}

	public void setCheckWhoLastConnected(String checkWhoLastConnected) {
		this.checkWhoLastConnected = checkWhoLastConnected;
	}

	public HashMap<Integer, String> getCheckNumberOfLastModificationPerUser() {
		return checkNumberOfLastModificationPerUser;
	}

	public void setCheckNumberOfLastModificationPerUser(
			HashMap<Integer, String> checkNumberOfLastModificationPerUser) {
		this.checkNumberOfLastModificationPerUser = checkNumberOfLastModificationPerUser;
	}

	public HashMap<String, Integer> getCountNumberOfConceptPerRelationship() {
		return countNumberOfConceptPerRelationship;
	}

	public void setCountNumberOfConceptPerRelationship(
			HashMap<String, Integer> countNumberOfConceptPerRelationship) {
		this.countNumberOfConceptPerRelationship = countNumberOfConceptPerRelationship;
	}

	public HashMap<String, HashMap<String, ArrayList<ClassObject>>> getListTheDomainAndRangeForRelationship() {
		return listTheDomainAndRangeForRelationship;
	}

	public void setListTheDomainAndRangeForRelationship(
			HashMap<String, HashMap<String, ArrayList<ClassObject>>> listTheDomainAndRangeForRelationship) {
		this.listTheDomainAndRangeForRelationship = listTheDomainAndRangeForRelationship;
	}

	public HashMap<String, Integer> getCountNumberOfExports() {
		return countNumberOfExports;
	}

	public void setCountNumberOfExports(
			HashMap<String, Integer> countNumberOfExports) {
		this.countNumberOfExports = countNumberOfExports;
	}

	
	
}
