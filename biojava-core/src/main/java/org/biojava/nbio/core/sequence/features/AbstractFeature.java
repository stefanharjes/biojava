/*
 *					BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *	  http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *	  http://www.biojava.org/
 *
 * Created on 01-21-2010
 */

package org.biojava.nbio.core.sequence.features;

import org.biojava.nbio.core.sequence.location.template.AbstractLocation;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.biojava.nbio.core.sequence.template.Compound;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A feature is currently any descriptive item that can be associated with a sequence position(s)
 * A feature has a type and a source which is currently a string to allow flexibility for the user
 * Ideally well defined features should have a class to describe attributes of that feature
 * @author Scooter Willis <willishf at gmail dot com>
 */
public abstract class AbstractFeature<S extends AbstractSequence<C>, C extends Compound>
		implements FeatureInterface<S, C> {
	List<FeatureInterface<S, C>> childrenFeatures = new ArrayList<FeatureInterface<S, C>>();
	FeatureInterface<S, C> parentFeature;
	AbstractLocation sequenceLocation;
	String type = "";
	String source = "";
	private String description = "";
	private String shortDescription = "";
	private Object userObject = null;
	private GenBankQualifierMap qualifierMap = new GenBankQualifierMap();
	//private Map<String, List<Qualifier>> Qualifiers = new HashMap<String, List<Qualifier>>();

	/**
	 * A feature has a type and a source
	 * @param type
	 * @param source
	 */
	public AbstractFeature(String type,String source){
		this.type = type;
		this.source = source;
	}

	/**
	 * A feature could be a single sequence position like a mutation or a post translational modification of an amino acid.
	 * It could also be the docking interface of N number of amino acids on the surface. The location wold then be a collection
	 * of sequence positions instead of a single sequence position or the begin and end of a sequence segment.
	 * @return
	 */

	@Override
	public AbstractLocation getLocations() {
		return sequenceLocation;
	}

	/**
	 *  A feature could be a single sequence position like a mutation or a post translational modification of an amino acid.
	 * It could also be the docking interface of N number of amino acids on the surface. The location wold then be a collection
	 * of sequence positions instead of a single sequence position or the begin and end of a sequence segment.
	 * @param loc
	 */
	@Override
	public void setLocation(AbstractLocation loc) {
		sequenceLocation = loc;
	}

	/**
	 * The feature type
	 * @return
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * Set the feature type
	 * @param type
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * The feature source
	 * @return
	 */

	@Override
	public String getSource() {
		return source;
	}

	/**
	 * Set the feature source
	 * @param source
	 */
	@Override
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * A feature can be the child or contained by a parent feature. An example is a Helix feature could contain
	 * children features. A PFAM domain could contain secondary structures.
	 * @param feature
	 */
	@Override
	public void setParentFeature(FeatureInterface<S, C> feature) {
		parentFeature = feature;
	}

	/**
	 * Get the parent Feature
	 * @return
	 */
	@Override
	public FeatureInterface<S, C> getParentFeature() {
	   return parentFeature;
	}

	/**
	 * Get the children features
	 * @return
	 */
	@Override
	public List<FeatureInterface<S, C>> getChildrenFeatures() {
		return childrenFeatures;
	}

	/**
	 * Set the children features
	 * @param features
	 */
	@Override
	public void setChildrenFeatures(List<FeatureInterface<S, C>> features) {
		childrenFeatures = features;

	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the shortDescription
	 */
	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription the shortDescription to set
	 */
	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Sort features by start position and then longest length. When features are added
	 * having them sorted by start position and then longest length helps on the layout
	 * of overlapping features so they are delivered in a proper order.
	 */

	public static final Comparator<FeatureInterface<?, ?>> LOCATION_LENGTH = new Comparator<FeatureInterface<?, ?>>() {

		@Override
		public int compare(FeatureInterface<?, ?> e1, FeatureInterface<?, ?> e2) {
			double v1 = e1.getLocations().getStart().getPosition();
			double v2 = e2.getLocations().getStart().getPosition();
			if (v1 < v2) {
				return -1;
			} else if (v1 > v2) {
				return 1;
			} else {
				double end1 = e1.getLocations().getEnd().getPosition();
				double end2 = e2.getLocations().getEnd().getPosition();
				if(end1 > end2)
					return -1;
				else if(end1 < end2)
					return 1;
				else
				return 0;
			}

		}
	};

	 /**
	 * Sort features by length. //TODO need to handle cases where features have multiple locations, strand etc
	 *
	 */

	static public final Comparator<FeatureInterface<?, ?>> LENGTH = new Comparator<FeatureInterface<?, ?>>() {

		@Override
		public int compare(FeatureInterface<?, ?> e1, FeatureInterface<?, ?> e2) {
			double v1 = Math.abs(e1.getLocations().getEnd().getPosition()- e1.getLocations().getStart().getPosition());
			double v2 = Math.abs(e2.getLocations().getEnd().getPosition() -  e2.getLocations().getStart().getPosition());
			if (v1 < v2) {
				return -1;
			} else if (v1 > v2) {
				return 1;
			} else {
				return 0;
			}

		}
	};
	
	/**
	 * Sort features by type
	 */
	public static final Comparator<FeatureInterface<?, ?>> TYPE = new Comparator<FeatureInterface<?, ?>>() {

		@Override
		public int compare(FeatureInterface<?, ?> o1, FeatureInterface<?, ?> o2) {
			return o1.getType().compareTo(o2.getType());
		}
	};

	/**
	 * @return the userObject
	 */
	@Override
	public Object getUserObject() {
		return userObject;
	}

	/**
	 * Allow the user to associate an object with the feature. This way if a feature which is displayed in a GUI
	 * is clicked on the application can then get a user defined object associated with the feature.
	 * @param userObject the userObject to set
	 */
	@Override
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
	/**
	 * map implementation to store qualifiers where only qualifier hold its key and value pair
	 * @return
	 */
	public GenBankQualifierMap getQualifierMap() {
		return qualifierMap;
	}
	/**
	 * get all qualifiers of this feature
	 * @return
	 */
	public Qualifier[] getQualifiers() {
		return qualifierMap.entrySet();
	}
	/**
	 * overwrite qualifiermap
	 * @param qualifierMap
	 */
	public void setQualifierMap(GenBankQualifierMap qualifierMap) {
		this.qualifierMap = qualifierMap;
	}
	/**
	 * overwrite qualifiers 
	 * @param qualifiers
	 */
	public void setQualifiers(Qualifier[] qualifiers) {
		this.qualifierMap=new GenBankQualifierMap(qualifiers);
	}
	/**
	 * overwrite this qualifier 
	 * @param qualifiers
	 */
	public void setQualifier(Qualifier q) {
		qualifierMap.set(q);
	}
	/**
	 * add this qualifier
	 * @param qualifier
	 */
	public void addQualifier(Qualifier qualifier) {
		qualifierMap.add(qualifier);
	}
	/**
	 * add a bunch of qualifiers  
	 * @param qa
	 */
	public void addQualifiers(Qualifier[] qa) {
		for(Qualifier q:qa) this.addQualifier(q);
	}
	/**
	 * get qualifier by name
	 * 
	 */
	public Qualifier getQualifierByName(String qName) { return qualifierMap.getQualifierNyName(qName); }
	/**
	 * get the first qualifier which has the given value
	 */
	public Qualifier getFirstQualifierByValue(String value) { return qualifierMap.getFirstQualifierByValue(value); };
	/**
	 * get all qualifiers which have the given value
	 */
	public Qualifier[] getQualifiersByValue(String value) { return qualifierMap.getQualifiersByValue(value); };
	//
	//db reference info stuff 
	//
	/*
	 * returns all database names and the sequence references for the corresponding database in a String[][2]
	 * @return
	 *
	public String[][] getAllDatabasesReferenceInfos() {
		DBReferenceInfo dbRefI =  this.qualifierMap.getDBReferenceInfo();
		if(dbRefI!=null) return dbRefI.getAllDatabaseReferenceInfos();
		else return null;
	}*/
	/**
	 * returns the dbreferenceinfo of this feature, which can contain lots of 
	 * entries 
	 * * @return
	 */
	public DBReferenceInfo getAllDatabaseReferenceInfos() {
		return this.qualifierMap.getDBReferenceInfo();
	}
	/**
	 * returns all databases of this feature in a string[]
	 * @return
	 */
	public String[] getAllDatabases() {
		DBReferenceInfo dbRefI =  this.qualifierMap.getDBReferenceInfo();
		if(dbRefI!=null) return dbRefI.getAllDatabases();		
		else return null;
	}
	/**
	 * returns all sequence database references for all databases in a string[]
	 * for this feature 
	 * @return
	 */
	public String[] getAllDatabaseReferences() {
		DBReferenceInfo dbRefI = this.qualifierMap.getDBReferenceInfo();
		if(dbRefI!=null) return dbRefI.getAllDatabaseReferences();
		else return null;
	}
	/**
	 * get database reference info #i as new DBReferenceInfo
	 * @param i
	 * @return
	 */
	public DBReferenceInfo getDatabaseReferenceInfo(int i) {
		DBReferenceInfo dbRefI = this.qualifierMap.getDBReferenceInfo();
		if(dbRefI!=null) return new DBReferenceInfo(dbRefI.getDatabaseReferenceInfo(i));
		else return null;
	}
	/**
	 * get database #i 
	 * @param i
	 * @return
	 */
	public String getDatabase(int i) {
		DBReferenceInfo dbRefI = this.qualifierMap.getDBReferenceInfo();
		if(dbRefI!=null) return dbRefI.getDatabase(i);
		else return null;
	}
	/**
	 * get sequence database reference #i
	 * @param i
	 * @return
	 */
	public String getDatabaseReference(int i) {
		DBReferenceInfo dbRefI = this.qualifierMap.getDBReferenceInfo();
		if(dbRefI!=null) return dbRefI.getDatabaseReference(i);
		else return null;
	}
	/**
	 * convenience method to point out that there can be several
	 * databases each with several references. This method returns
	 * only one database with one reference
	 * 
	 * @return
	 */
	public DBReferenceInfo getFirstDatabaseReferenceInfo() {
		return getDatabaseReferenceInfo(0);
	}
	/**
	 * convenience method to point out that there can be several
	 * database references per database
	 * @return
	 */
	public String getFirstDatabaseReference() {
		return getDatabaseReference(0);
	}
	/**
	 * convenience method to point out that there can be several 
	 * databases
	 * @return
	 */
	public String getFirstDatabase() {
		return getDatabase(0);
	}
	/**
	 * get the sequence record i for the database with name database
	 * @param database
	 * @return null if no such record
	 */
	public String getDatabaseReference(String database, int i) {
		DBReferenceInfo dbRefI = this.qualifierMap.getDBReferenceInfo();
		if(dbRefI!=null) return dbRefI.getDatabaseReference(database,i); 
		else return null;
	}
	/**
	 * get all references of a sequence in a particular database
	 * in the same database
	 * @param database
	 * @return string[] of all references
	 */
	public String[] getAllDatabaseReferences(String database) {
		DBReferenceInfo dbRefI = this.qualifierMap.getDBReferenceInfo();
		if(dbRefI!=null) return dbRefI.getAllDatabaseReferences(database); 
		else return null;
	}

	/**
	 * convenience method to point out that a sequence can have several database references
	 * in the same database
	 * @param database
	 * @return
	 */
	public String getFirstDatabaseReference(String database) {
		return getDatabaseReference(database, 0);
	}

	public void setDatabaseReferenceInfo(DBReferenceInfo dbRefI) {
		qualifierMap.set(dbRefI);
	}
	
	public void addDatabaseReferenceInfo(DBReferenceInfo dbRefI) {
		this.qualifierMap.add(dbRefI);
	}
	/**
	 * 
	 *  @Deprecated use addQualifier(Qualifier q)
	 */
	@Deprecated
	public void addQualifier(String str, Qualifier q) {
		this.qualifierMap.add(q);
	}
}
