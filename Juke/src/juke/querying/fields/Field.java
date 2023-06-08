package juke.querying.fields;

import juke.querying.QueryElement;

/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 09.01.12
 * Time: 1:11
 * To change this juke.sqliteAndroid.templating use File | Settings | File Templates.
 */
public abstract class Field extends QueryElement
{
    private String alias;

    public String getAlias()
    {
        return alias;
    }
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
}
