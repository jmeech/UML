package application.include;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import application.include.Model.classStackData;
import application.objects.ClassBlock;
import application.objects.Link;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class Model {
	private Stack<classStackData> classUndoStack = new Stack<classStackData>();
	private Stack<classStackData> classRedoStack = new Stack<classStackData>();
	private Stack<Integer> classUndoStackSize = new Stack<Integer>();
	private Stack<Integer> classRedoStackSize = new Stack<Integer>();
	private Stack<linkStackData> linkUndoStack = new Stack<linkStackData>();
	private Stack<linkStackData> linkRedoStack = new Stack<linkStackData>();
	private Stack<Integer> linkUndoStackSize = new Stack<Integer>();
	private Stack<Integer> linkRedoStackSize = new Stack<Integer>();

	private Boolean duringUndo = false;
	private Boolean duringRedo = false;
	private Boolean justRestoredState = false;
	private Boolean justUndid = false;
	private Boolean justRedid = false;

	public class classStackData {
		private int[] intData = new int[5];
		private String name;
		private String attr;
		private String oper;
		private String desc;
	}

	public class linkStackData {
		private int[] intData = new int[8];
		private String label;
	}

	public class ClassModel {

		/*
		 * intData: [index] [Position (x)] [Position (y)] [Width] [Height] Index is used
		 * for reference. Positions x & y are used for class block placement. I have
		 * them set to automatically round any given value to the nearest 10, but
		 * changing STEP will change the grid size if 10 doesn't look right. Width and
		 * Height should be obvious as well. They're using the same formula as X and Y,
		 * so they should adhere to the grid as well. stringData: [Name] [Attributes]
		 * [Operations] [Descriptions] The four elements in stringData should be pretty
		 * self explanatory. Depending on how we need to display the data, it might be
		 * easier to have a lone string (Name) and an array[3] of linked lists, so the
		 * lists of entries can be expanded indefinitely.
		 */
		private int[] intData = new int[5];
		private StringProperty name = new SimpleStringProperty();
		private StringProperty attr = new SimpleStringProperty();
		private StringProperty oper = new SimpleStringProperty();
		private StringProperty desc = new SimpleStringProperty();
		private final int STEP = 10;

		/**
		 * Constructs an instance of ClassModel
		 * 
		 * @constructor
		 * @param intsIn
		 *            the array of ints to be stored in the model
		 * @param stringsIn
		 *            the array of Strings to be stored in the model
		 */
		public ClassModel(int[] intsIn, String[] stringsIn) {
			if (intsIn.length == 5) {
				intData = intsIn;
			}

			if (stringsIn.length == 4) {
				name.set(stringsIn[0]);
				attr.set(stringsIn[1]);
				oper.set(stringsIn[2]);
				desc.set(stringsIn[3]);
			}
		}

		/*****************************
		 * SETTERS
		 ****************************/

		/**
		 * Sets the index value of the ClassModel
		 * 
		 * @param i
		 *            the index to be stored
		 */
		public void setIndex(int i) {
			intData[0] = i;
		}

		/**
		 * Sets the x position value of the ClassModel
		 * 
		 * @param x
		 *            the x position value to be stored
		 */
		public void setXPos(int x) {
			if (x >= 0) {
				intData[1] = (x % STEP < (STEP / 2) ? x - (x % STEP) : x + STEP - (x % STEP));
			} else {
				intData[1] = 0;
			}
		}

		/**
		 * Sets the y position value of the ClassModel
		 * 
		 * @param y
		 *            the y position value to be stored
		 */
		public void setYPos(int y) {
			if (y >= 0) {
				intData[2] = (y % STEP < (STEP / 2) ? y - (y % STEP) : y + STEP - (y % STEP));
			} else {
				intData[2] = 0;
			}
		}

		/**
		 * Sets the width value of the ClassModel
		 * 
		 * @param w
		 *            the width value to be stored
		 */
		public void setWidth(int w) {
			intData[3] = (w % STEP < (STEP / 2) ? w - (w % STEP) : w + STEP - (w % STEP));
		}

		/**
		 * Sets the height value of the ClassModel
		 * 
		 * @param h
		 *            the height value to be stored
		 */
		public void setHeight(int h) {
			intData[4] = (h % STEP < (STEP / 2) ? h - (h % STEP) : h + STEP - (h % STEP));
		}

		/**
		 * Sets the name value of the ClassModel
		 * 
		 * @param n
		 *            the name value to be stored
		 */
		public void setName(String n) {
			name.set(n);
		}

		/**
		 * Sets the attributes value of the ClassModel
		 * 
		 * @param a
		 *            the attributes value to be stored
		 */
		public void setAttr(String a) {
			attr.set(a);
		}

		/**
		 * Sets the operations value of the ClassModel
		 * 
		 * @param o
		 *            the operations valie to be stored
		 */
		public void setOper(String o) {
			oper.set(o);
		}

		/**
		 * Sets the description value of the ClassModel
		 * 
		 * @param d
		 *            the desctiption model to be stored
		 */
		public void setDesc(String d) {
			desc.set(d);
		}

		/*****************************
		 * GETTERS
		 ****************************/

		/**
		 * Returns the index value of the ClassModel
		 * 
		 * @return the index value of the ClassModel
		 */
		public int getIndex() {
			return intData[0];
		}

		/**
		 * Returns the x position value of the ClassModel
		 * 
		 * @return the x position value of the ClassModel
		 */
		public int getXPos() {
			return intData[1];
		}

		/**
		 * Returns the y position value of the ClassModel
		 * 
		 * @return the y position value of the ClassModel
		 */
		public int getYPos() {
			return intData[2];
		}

		/**
		 * Returns the width value of the ClassModel
		 * 
		 * @return the width value of the ClassModel
		 */
		public int getWidth() {
			return intData[3];
		}

		/**
		 * Returns the height value of the ClassModel
		 * 
		 * @return the height value of the ClassModel
		 */
		public int getHeight() {
			return intData[4];
		}

		/**
		 * Returns the name value of the ClassModel
		 * 
		 * @return the name value of the ClassModel
		 */
		public String getName() {
			return name.get();
		}

		/**
		 * Returns the attributes value of the ClassModel
		 * 
		 * @return the attributes value of the ClassModel
		 */
		public String getAttr() {
			return attr.get();
		}

		/**
		 * Returns the operations value of the ClassModel
		 * 
		 * @return the operations value of the ClassModel
		 */
		public String getOper() {
			return oper.get();
		}

		/**
		 * Returns the description value of the ClassModel
		 * 
		 * @return the description value of the ClassModel
		 */
		public String getDesc() {
			return desc.get();
		}

		/**
		 * Returns the StringProperty associated with the ClassModel's name
		 * 
		 * @return the StringProperty associated with the ClassModel's name
		 */
		public StringProperty getNameProp() {
			return name;
		}

		/**
		 * Returns the StringProperty associated with the ClassModel's attributes
		 * 
		 * @return the StringProperty associated with the ClassModel's attributes
		 */
		public StringProperty getAttrProp() {
			return attr;
		}

		/**
		 * Returns the StringProperty associated with the ClassModel's operations
		 * 
		 * @return the StringProperty associated with the ClassModel's operations
		 */
		public StringProperty getOperProp() {
			return oper;
		}

		/**
		 * Returns the StringProperty associated with the ClassModel's description
		 * 
		 * @return the StringProperty associated with the ClassModel's description
		 */
		public StringProperty getDescProp() {
			return desc;
		}
	}

	public class LinkModel {
		/*
		 * intData: [Connection index] [Connection type] [Source] [Dest] [Source
		 * minimum][Source maximum] [Destination minimum] [Destination Maximum] Index is
		 * used for reference Connection type denotes the type of connection: 0 -
		 * general 1 - association 2 - aggregation 3 - composition 4 - generalization 5
		 * - dependency Source and Destination store the indices of the Source and
		 * Destination blocks respectively. Source minimum and source maximum denote the
		 * cardinality of the connection with the source class block (ie. 0 - 1, 0 - *).
		 * Use -1 (or any negative) to denote ANY (*). Destination minimum and
		 * destination maximum denote the cardinality of the connection with the
		 * destination block. Same rules apply. Label is pretty straightforward.
		 */
		private int[] intData = new int[6];
		private IntegerProperty src = new SimpleIntegerProperty();
		private IntegerProperty dest = new SimpleIntegerProperty();
		private String label;

		/**
		 * Constructs an instance of LinkModel
		 * 
		 * @constructor
		 * @param data
		 *            the integral data to be stored
		 * @param l
		 *            the label to be stored
		 */
		public LinkModel(int[] data, String l) {
			if (data.length == 8) {
				intData[0] = data[0];
				intData[1] = data[1];
				src.set(data[2]);
				dest.set(data[3]);
				intData[2] = data[4];
				intData[3] = data[5];
				intData[4] = data[6];
				intData[5] = data[7];

			}
			label = l;
		}

		/*****************************
		 * SETTERS
		 ****************************/

		/**
		 * Sets the index value of the LinkModel
		 * 
		 * @param i
		 *            the index value to be stored
		 */
		public void setIndex(int i) {
			intData[0] = i;
		}

		/**
		 * Sets the type value of the LinkModel
		 * 
		 * @param t
		 *            the type value to be stored
		 */
		public void setType(int t) {
			intData[1] = t;
		}

		/**
		 * Sets the source index of the LinkModel
		 * 
		 * @param s
		 *            the source index to be stored
		 */
		public void setSource(int s) {
			src.set(s);
		}

		/**
		 * Sets the destination index of the LinkModel
		 * 
		 * @param d
		 *            the destination index to be stored
		 */
		public void setDest(int d) {
			dest.set(d);
		}

		/**
		 * Sets the source minimum value of the LinkModel
		 * 
		 * @param s
		 *            the source minimum value to be stored
		 */
		public void setSourceMin(int s) {
			intData[2] = s;
		}

		/**
		 * Sets the source maximum value of the LinkModel
		 * 
		 * @param s
		 *            the source maximum value to be stored
		 */
		public void setSourceMax(int s) {
			intData[3] = s;
		}

		/**
		 * Sets the destination minimum value of the LinkModel
		 * 
		 * @param d
		 *            the destination minimum value to be stored
		 */
		public void setDestMin(int d) {
			intData[4] = d;
		}

		/**
		 * Sets the destination maximum value of the LinkModel
		 * 
		 * @param d
		 *            the destination maximum value to be stored
		 */
		public void setDestMax(int d) {
			intData[5] = d;
		}

		/**
		 * Sets the label of the LinkModel
		 * 
		 * @param l
		 *            the label to be stored
		 */
		public void setLabel(String l) {
			label = l;
		}

		/*****************************
		 * GETTERS
		 ****************************/

		/**
		 * Returns the index value of the LinkModel
		 * 
		 * @return the index value of the LinkModel
		 */
		public int getIndex() {
			return intData[0];
		}

		/**
		 * Returns the type value of the LinkModel
		 * 
		 * @return the type value of the LinkModel
		 */
		public int getType() {
			return intData[1];
		}

		/**
		 * Returns the source index of the LinkModel
		 * 
		 * @return the source index of the LinkModel
		 */
		public int getSource() {
			return src.get();
		}

		/**
		 * Returns the destination index of the LinkModel
		 * 
		 * @return the destination index of the LinkModel
		 */
		public int getDest() {
			return dest.get();
		}

		/**
		 * Returns the source minimum value of the LinkModel
		 * 
		 * @return the source minimum value of the LinkModel
		 */
		public int getSourceMin() {
			return intData[2];
		}

		/**
		 * Returns the source maximum value of the LinkModel
		 * 
		 * @return the source maximum value of the LinkModel
		 */
		public int getSourceMax() {
			return intData[3];
		}

		/**
		 * Returns the destination minimum value of the LinkModel
		 * 
		 * @return the destination minimum value of the LinkModel
		 */
		public int getDestMin() {
			return intData[4];
		}

		/**
		 * Returns the destination maximum value of the LinkModel
		 * 
		 * @return the destination maximum value of the LinkModel
		 */
		public int getDestMax() {
			return intData[5];
		}

		/**
		 * Returns the label of the LinkModel
		 * 
		 * @return the label of the LinkModel
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * Returns the IntegerProperty associated with the LinkModel's source
		 * 
		 * @return the IntegerProperty associated with the LinkModel's source
		 */
		public IntegerProperty getSourceProp() {
			return src;
		}

		/**
		 * Returns the IntegerProperty associated with the LinkModel's destination
		 * 
		 * @return the IntegerProperty associated with the LinkModel's destination
		 */
		public IntegerProperty getDestProp() {
			return dest;
		}
	}

	private ObservableList<ClassModel> classList;
	private ObservableList<LinkModel> linkList;
	private List<ClassBlock> classes;
	private List<Link> links;

	/*
	 * This class uses the classList and connectionList classes to represent all the
	 * elements being stored in the diagram. Using two separate classes to store
	 * this data will, I think, make it easier to extend if we get to the point
	 * where we want to add additional features, as well as providing a relatively
	 * simple structure to dump and reload in order to save and load files.
	 */

	/**
	 * Constructs an instance of Model
	 * 
	 * @constructor
	 */
	public Model() {
		classList = FXCollections.observableArrayList(new Callback<ClassModel, Observable[]>() {
			@Override
			public Observable[] call(ClassModel param) {
				return new Observable[] { param.getNameProp(), param.getAttrProp(), param.getOperProp(),
						param.getDescProp() };
			}
		});

		linkList = FXCollections.observableArrayList(new Callback<LinkModel, Observable[]>() {
			@Override
			public Observable[] call(LinkModel param) {
				return new Observable[] { param.getSourceProp(), param.getDestProp() };
			}
		});

		classes = new ArrayList<ClassBlock>();
		links = new ArrayList<Link>();
	}

	/**
	 * Returns the classList property
	 * 
	 * @return classList
	 */
	public ObservableList<ClassModel> getClassProperty() {
		return classList;
	}

	/**
	 * Returns the linkList property
	 * 
	 * @return linkList
	 */
	public ObservableList<LinkModel> getLinkProperty() {
		return linkList;
	}

	/**
	 * Returns the smallest index that isn't presently storing a ClassModel object.
	 * 
	 * @return an int corresponding with the tail of the list
	 */
	public int getClassTail() {
		return classList.size();
	}

	/**
	 * Returns the smallest index that isn't presently storing a LinkModel object,
	 * 
	 * @return an int corresponding with the tail of the list
	 */
	public int getLinkTail() {
		return linkList.size();
	}

	/**
	 * Returns the ClassModel object stored at index i
	 * 
	 * @param i
	 *            the index of the ClassModel object to be returned
	 * @return the ClassModel object stored at index i
	 */
	public ClassModel getClassModel(int i) {
		return classList.get(i);
	}

	/**
	 * Returns the LinkModel object stored at index i
	 * 
	 * @param i
	 *            the index of the LinkModel object to be returned
	 * @return the LinkModel object stored at index i
	 */
	public LinkModel getLinkModel(int i) {
		return linkList.get(i);
	}

	/**
	 * Returns the ClassBlock object stored at index i
	 * 
	 * @param i
	 *            the index of the ClassBlock object to be returned
	 * @return the ClassBlock object stored at index i
	 */
	public ClassBlock getClass(int i) {
		return classes.get(i);
	}

	/**
	 * Returns the Link object stored at index i
	 * 
	 * @param i
	 *            the index of the Link object to be returned
	 * @return the Link object stored at index i
	 */
	public Link getLink(int i) {
		return links.get(i);
	}

	/**
	 * Creates a new ClassModel object and places it at the end of the list.
	 * 
	 * @param ints
	 *            A list of int arguments to be passed to the ClassModel
	 *            constructor.
	 * @param strings
	 *            A list of String arguments to be passed to the ClassModel
	 *            constructor.
	 * @return the index of the new ClassModel object
	 */
	public int addClassModel(int[] ints, String[] strings) {
		if (ints.length == 5 && strings.length == 4) {
			classList.add(new ClassModel(ints, strings));
		}
		return (classList.size() - 1);
	}

	/**
	 * Removes the ClassModel object stored at index i and updates the linkList to
	 * reflect the index changes.
	 * 
	 * @param i
	 *            the index of the ClassModel to be removed
	 */
	public void removeClassModel(int i) {
		classList.remove(i);
	}

	/**
	 * Removes the LinkModel object stored at index i and decrements later indices
	 * 
	 * @param i
	 *            the index of the LinkModel to be removed
	 */
	public void removeLinkModel(int i) {
		links.get(i).warnLinkNodes();
		linkList.remove(i);
		for (int l = i; l != linkList.size(); ++l) {
			linkList.get(l).setIndex(l);
		}
	}

	/**
	 * Creates a new LinkModel object and places it at the end of the list.
	 * 
	 * @param ints
	 *            A list of int arguments to be passed to the ConnectionModel
	 *            constructor.
	 * @param label
	 *            The label to be passed to the ConnectionModel constructor.
	 * @return the index of the new ConnectionModel object
	 */
	public int addLinkModel(int[] ints, String label) {
		if (ints.length == 8) {
			linkList.add(new LinkModel(ints, label));
		}
		return (linkList.size() - 1);
	}

	/**
	 * Stores a Class Block object in the classes list
	 * 
	 * @param in
	 *            The Class Block to be stored
	 * 
	 */
	public void addClass(ClassBlock in) {
		classes.add(in);
	}

	/**
	 * Removes the ClassBlock object stored at index i
	 * 
	 * @param i
	 *            The index of the ClassBlock to be removed
	 */
	public void removeClass(int i) {
		classes.remove(i);
	}

	/**
	 * Stores a Link object in the links list
	 * 
	 * @param in
	 *            The Link object to be stored
	 * 
	 */
	public void addLink(Link in) {
		links.add(in);
	}

	/**
	 * Removes the Link object stored at index i
	 * 
	 * @param i
	 *            The index of the Link to be removed
	 */
	public void removeLink(int i) {
		links.remove(i);
	}

	/**
	 * Clears all Links from the links list
	 */
	public void clearLinks() {
		for (Link linky : links)
			linky.warnLinkNodes();

		links.clear();
	}

	/**
	 * Tells if it is safe to saveUndoState
	 * 
	 * @return is false if model is in the middle of a undo or redo
	 */
	public Boolean safeToSave() {
		return !(duringUndo || duringRedo);
	}

	/**
	 * 
	 */
	public void setUndoState() {
		duringUndo = true;

	}

	/**
	 * 
	 */
	public void setRedoState() {
		duringRedo = true;
	}
	/**
	 * 
	 * @return
	 */
	public Boolean getJustRedid() {
		return justRedid;
	}

	/**
	 * 
	 * Saves an undoState onto the classUndoStack
	 * 
	 */
	public void saveUndoState() {
		if (classList.size() != 0) {
			classUndoStackSize.push(classList.size());

			for (int i = 0; i != classList.size(); ++i) {
				classStackData state = new classStackData();

				state.intData[0] = classList.get(i).getIndex();
				state.intData[1] = classList.get(i).getXPos();
				state.intData[2] = classList.get(i).getYPos();
				state.intData[3] = classList.get(i).getWidth();
				state.intData[4] = classList.get(i).getHeight();
				state.name = classList.get(i).getName();
				state.attr = classList.get(i).getAttr();
				state.oper = classList.get(i).getOper();
				state.desc = classList.get(i).getDesc();

				classUndoStack.push(state);
			}

			linkUndoStackSize.push(linkList.size());

			for (int i = 0; i != linkList.size(); ++i) {
				linkStackData lstate = new linkStackData();

				lstate.intData[0] = linkList.get(i).getIndex();
				lstate.intData[1] = linkList.get(i).getType();
				lstate.intData[2] = linkList.get(i).getSource();
				lstate.intData[3] = linkList.get(i).getDest();
				lstate.intData[4] = linkList.get(i).getSourceMin();
				lstate.intData[5] = linkList.get(i).getSourceMax();
				lstate.intData[6] = linkList.get(i).getDestMin();
				lstate.intData[7] = linkList.get(i).getDestMax();
				lstate.label = linkList.get(i).getLabel();

				linkUndoStack.push(lstate);
			}

			System.out.println("Saved state. Undo stacks:");
			System.out.println(classUndoStackSize.toString());
			System.out.println(linkUndoStackSize.toString());

			justRestoredState = false;
			justUndid = false;
			justRedid = false;
		}
	}

	/**
	 * 
	 */
	public void saveRedoState() {
		if (classList.size() != 0) {
			classRedoStackSize.push(classList.size());

			for (int i = 0; i != classList.size(); ++i) {
				classStackData state = new classStackData();

				state.intData[0] = classList.get(i).getIndex();
				state.intData[1] = classList.get(i).getXPos();
				state.intData[2] = classList.get(i).getYPos();
				state.intData[3] = classList.get(i).getWidth();
				state.intData[4] = classList.get(i).getHeight();
				state.name = classList.get(i).getName();
				state.attr = classList.get(i).getAttr();
				state.oper = classList.get(i).getOper();
				state.desc = classList.get(i).getDesc();

				classRedoStack.push(state);
			}

			linkRedoStackSize.push(linkList.size());

			for (int i = 0; i != linkList.size(); ++i) {
				linkStackData lstate = new linkStackData();

				lstate.intData[0] = linkList.get(i).getIndex();
				lstate.intData[1] = linkList.get(i).getType();
				lstate.intData[2] = linkList.get(i).getSource();
				lstate.intData[3] = linkList.get(i).getDest();
				lstate.intData[4] = linkList.get(i).getSourceMin();
				lstate.intData[5] = linkList.get(i).getSourceMax();
				lstate.intData[6] = linkList.get(i).getDestMin();
				lstate.intData[7] = linkList.get(i).getDestMax();
				lstate.label = linkList.get(i).getLabel();

				linkRedoStack.push(lstate);
			}

			System.out.println("Saved REDO state. REDO stacks:");
			System.out.println(classRedoStackSize.toString());
			System.out.println(linkRedoStackSize.toString());
		}
	}
	
	public void undo() {
		this.saveRedoState();
		
		if (!classUndoStackSize.isEmpty()) {
			// first erase the state that was JUST saved (what the user wants to get rid of)
			/*
			if (!justRestoredState) {
				int throwaway = classUndoStackSize.pop();

				// unique case
				if (classUndoStackSize.isEmpty()) {
					classRedoStackSize.push(throwaway);
					for (int i = 0; i != throwaway; ++i)
						classRedoStack.push(classUndoStack.pop());
				} else
					for (int i = 0; i != throwaway; ++i)
						classUndoStack.pop();

			}*/

			if (!classUndoStackSize.isEmpty()) {
				// now actually restore the previous state
				int size = classUndoStackSize.pop();
				//classRedoStackSize.push(size);

				for (int i = 0; i != size; ++i) {
					classStackData state = classUndoStack.pop();
					//classRedoStack.push(state);

					int[] ints = { state.intData[0], state.intData[1], state.intData[2], state.intData[3],
							state.intData[4] };

					String[] strings = { state.name, state.attr, state.oper, state.desc };
					classList.add(new ClassModel(ints, strings));
				}
			}
		}

		if (!linkUndoStackSize.isEmpty()) {
			// now erase the state that was JUST saved (what the user wants to get rid of)
			/*if (!justRestoredState) {
				int throwaway = linkUndoStackSize.pop();
				// unique case
				if (linkUndoStackSize.isEmpty()) {
					linkRedoStackSize.push(throwaway);
					for (int i = 0; i != throwaway; ++i)
						linkRedoStack.push(linkUndoStack.pop());
				} else
					for (int i = 0; i != throwaway; ++i)
						linkUndoStack.pop();
			}*/

			if (!linkUndoStackSize.isEmpty()) {
				int size = linkUndoStackSize.pop();
				//linkRedoStackSize.push(size);

				for (int i = 0; i != size; ++i) {
					linkStackData lstate = linkUndoStack.pop();
					//linkRedoStack.push(lstate);

					int[] ints = { lstate.intData[0], lstate.intData[1], lstate.intData[2], lstate.intData[3],
							lstate.intData[4], lstate.intData[5], lstate.intData[6], lstate.intData[7] };

					String label = lstate.label;
					linkList.add(new LinkModel(ints, label));
				}
			}
		}

		duringUndo = false;
		justRestoredState = true;
		justUndid = true;
		justRedid = false;
		
		System.out.println("Undid. Undo stacks: ");
		System.out.println(classUndoStackSize.toString());
		System.out.println(linkUndoStackSize.toString());
		System.out.println("Redo stacks: ");
		System.out.println(classRedoStackSize.toString());
		System.out.println(linkRedoStackSize.toString());
	}

	/**
	 * 
	 * 
	 */
	public void redo() {
		System.out.println(classRedoStack.isEmpty());
		if (!classRedoStackSize.isEmpty()) {
			int size = classRedoStackSize.pop();

			for (int i = 0; i != size; ++i) {
				classStackData state = classRedoStack.pop();

				int[] ints = { state.intData[0], state.intData[1], state.intData[2], state.intData[3],
						state.intData[4] };

				String[] strings = { state.name, state.attr, state.oper, state.desc };
				classList.add(new ClassModel(ints, strings));
			}
		}

		if (!linkRedoStackSize.isEmpty()) {
			int size = linkRedoStackSize.pop();

			for (int i = 0; i != size; ++i) {
				linkStackData lstate = linkRedoStack.pop();

				int[] ints = { lstate.intData[0], lstate.intData[1], lstate.intData[2], lstate.intData[3],
						lstate.intData[4], lstate.intData[5], lstate.intData[6], lstate.intData[7] };

				String label = lstate.label;
				linkList.add(new LinkModel(ints, label));
			}
		}
		duringRedo = false;
		justRestoredState = true;
		justUndid = false;
		justRedid = true;

		System.out.println("Redid. Undo stacks: ");
		System.out.println(classUndoStackSize.toString());
		System.out.println(linkUndoStackSize.toString());
		System.out.println("Redo stacks: ");
		System.out.println(classRedoStackSize.toString());
		System.out.println(linkRedoStackSize.toString());

	}

	/**
	 * Saves the model data in a format that can be reread later.
	 * 
	 * @param file
	 *            The file to be written to.
	 * @throws IOException
	 *             Throws if the file can't be written to.
	 */
	public void save(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write("CLASSLIST_START\n");
		writer.write(classList.size() + "\n");
		for (int i = 0; i != classList.size(); ++i) {
			writer.write(classList.get(i).getIndex() + " ");
			writer.write(classList.get(i).getXPos() + " ");
			writer.write(classList.get(i).getYPos() + " ");
			writer.write(classList.get(i).getWidth() + " ");
			writer.write(classList.get(i).getHeight() + " \n");
			writer.write(classList.get(i).getName() + "\n\n");
			writer.write(classList.get(i).getAttr() + "\n\n");
			writer.write(classList.get(i).getOper() + "\n\n");
			writer.write(classList.get(i).getDesc() + "\n\n");
		}
		writer.write("CLASSLIST_END\n");
		writer.write("LINKLIST_BEGIN\n");
		writer.write(linkList.size() + "\n");
		for (int i = 0; i != linkList.size(); ++i) {
			writer.write(linkList.get(i).getIndex() + " ");
			writer.write(linkList.get(i).getType() + " ");
			writer.write(linkList.get(i).getSource() + " ");
			writer.write(linkList.get(i).getDest() + " ");
			writer.write(linkList.get(i).getSourceMin() + " ");
			writer.write(linkList.get(i).getSourceMax() + " ");
			writer.write(linkList.get(i).getDestMin() + " ");
			writer.write(linkList.get(i).getDestMax() + " \n");
			writer.write(linkList.get(i).getLabel() + "\n");
		}
		writer.write("LINKLIST_END\n");

		writer.close();
	}

	/**
	 * Reads in the model data and rebuilds the model.
	 * 
	 * @param file
	 *            The file to be read from.
	 * @throws IOException
	 *             Throws if the file can't be read from.
	 */
	public void load(File file) throws IOException {
		Scanner reader = new Scanner(file);
		reader.next();

		int size = Integer.parseInt(reader.next().trim());
		for (int i = 0; i != size; ++i) {

			reader.useDelimiter(" ");

			int[] ints = { Integer.parseInt(reader.next().trim()), Integer.parseInt(reader.next().trim()),
					Integer.parseInt(reader.next().trim()), Integer.parseInt(reader.next().trim()),
					Integer.parseInt(reader.next().trim()) };
			reader.useDelimiter("\n\n");
			String[] strings = { reader.next().trim(), reader.next(), reader.next(), reader.next() };
			classList.add(new ClassModel(ints, strings));
			reader.useDelimiter("\n");
			reader.next();

		}

		reader.next();
		reader.next();

		// From here down will almost certainly need to be rewritten once links are
		// implemented.
		size = Integer.parseInt(reader.next().trim());
		for (int i = 0; i != size; ++i) {
			reader.useDelimiter(" ");
			int[] ints = { Integer.parseInt(reader.next().trim()), Integer.parseInt(reader.next().trim()),
					Integer.parseInt(reader.next().trim()), Integer.parseInt(reader.next().trim()),
					Integer.parseInt(reader.next().trim()), Integer.parseInt(reader.next().trim()),
					Integer.parseInt(reader.next().trim()), Integer.parseInt(reader.next().trim()) };
			reader.useDelimiter("\n");
			reader.next();
			String label = reader.next().trim();

			linkList.add(new LinkModel(ints, label));
		}

		reader.close();
	}

	/**
	 * Clears the model of all data.
	 */
	public void clear() {
		classList.clear();
		classes.clear();
		linkList.clear();

		for (Link linky : links)
			linky.warnLinkNodes();

		links.clear();
	}

}
