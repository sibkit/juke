package juke.storage.sequence;

import juke.exceptions.JukeException;

/**
 * Created with IntelliJ IDEA.
 * User: chelovek
 * Date: 24.09.12
 * Time: 0:19
 * To change this template use File | Settings | File Templates.
 */
public interface Sequence<T>
{
	String getName();
	T getNextValue() throws JukeException;
	T getCurrentValue() throws JukeException;
}