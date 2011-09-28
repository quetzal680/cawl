package org.uwdl.mapper;

import java.util.Calendar;

import org.uwdl.entities.Entities;
import org.uwdl.entities.Entity;
import org.uwdl.entities.Sensor;
import org.uwdl.parser.UnvalidVariableException;
import org.uwdl.parser.ast.IConditionDescendant;
import org.uwdl.parser.ast.IRuleDescendant;
import org.uwdl.parser.ast.UCondition;
import org.uwdl.parser.ast.UConstraint;
import org.uwdl.parser.ast.UContext;
import org.uwdl.parser.ast.URule;


public class uWDLMapper {
	
	private boolean compare(Entities entities, UCondition condition) {
		IConditionDescendant descendant = condition.getDescendant();
		if (descendant instanceof UContext) {
			return compare(entities, (UContext)descendant);
		}
		return false;
	}
	
	private boolean compare(Entities entities, UContext context) {
		for (URule i: context.getRules()) {
			if (compare(entities, i)) return true;
		}
		return false;
	}
	
	// 마지막 조건만 맞으면 다 맞은거로 확인하고 참으로 판단하는 버그 존재
	private boolean compare(Entities entities, URule rule) {
		boolean test = false;
		for (IRuleDescendant i: rule.getConstraints()) {
			switch(i.getLogicTag()) {
			case CONSTRAINT: 
				test = compare(entities, (UConstraint)i.getConstraint()); 
				break;
			case AND:
				test &= compare(entities, (UConstraint)i.getConstraint()); 
				break;
			case OR:
				test |= compare(entities, (UConstraint)i.getConstraint()); 
				break;
			case NOT:
				test = !compare(entities, (UConstraint)i.getConstraint()); 
				break;
			}
		}
		return test;
	}
	
	private boolean compare(Entities entities, UConstraint constraint) {
		for (Entity i: entities.getEntities()) {
			if (compare(i, constraint)) return true;
		}
		return false;
	}
	
	private boolean compare(Entity entity, UConstraint constraint) {
		Boolean result = false;
		
		if( constraint.getVerb().getPcdata().equals("lt") )	{			
			int intSubject = Integer.parseInt(constraint.getSubject().getPcdata());
			int intObject;
			try {
				if( constraint.getObject().getPcdata().equals("NULL") ) {
					System.out.println("uWDLMapper.compare> \"lt\" Entity: " + entity.toString() + " : " + constraint);
					intObject = Integer.parseInt(entity.getObject().getValue());
				}
				else {
					intObject = Integer.parseInt(constraint.getObject().getPcdata());	
				}
				
				if( intSubject > intObject ) {
					result = true;
				}
			} catch( NumberFormatException e ) {
				System.out.println("uWDLMapper.compare> object : string");
			}
		}
		else if( constraint.getVerb().getPcdata().equals("gt") && constraint.getObject().getPcdata().equals("crtTime"))	{
			Calendar right = Calendar.getInstance();
			String time = Integer.toString(right.get(Calendar.HOUR_OF_DAY))
						+ Integer.toString(right.get(Calendar.MINUTE))
						+ Integer.toString(right.get(Calendar.SECOND));
			
			System.out.println("uWDLMapper.compare> \"is\" constraint : " + constraint);
			
			if( Integer.parseInt(constraint.getSubject().getPcdata()) < Integer.parseInt(time) ) {
				result = true;
			} else {
				result = false;
			}
		}
		else {
			 System.out.println("uWDLMapper.compare> Entity: " + entity.toString() + " : " + constraint);
			 result = ( entity.getSubject().equals(constraint.getSubject()) 
					 && entity.getVerb().equals(constraint.getVerb()) 
					 && entity.getObject().equals(constraint.getObject()));
		}
		
		return result;
	}
	
	public boolean find(Entities entities, UCondition condition) {
		return compare(entities, condition);
	}
	
/*	
	public URule find(Entities entities, UCondition condition) {
		// TODO default?
		for (UCase i: condition.getDescendant()) {
			if (compare(entities, i)) return i;
		}
		return null;
	}
	
	public UCondition find(Entities entities, UUWDL uWDL) {
		for (UActivator i: uWDL.getActivators()) {
			UCondition condition = find(entities, i);
			if (condition != null) return condition;
		}
		return null;
	}
*/	
}
