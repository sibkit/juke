/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package juke.querying;



import sibutils.common.Child;
import java.io.Serializable;
import java.util.List;


public abstract class QueryElement  implements Serializable, Child<QueryElement>
{
    private QueryElement parentElement;
    public abstract QueryElementType getElementType();

    public QueryElement getParentElement()
    {
        return parentElement;
    }

    protected String listToString(List list,String separator)
    {
        String result = "";
        for(Object o: list)
        {
            result+=""+o;
            if(list.indexOf(o)<list.size()-1)
            {
                result+=separator;
            }
        }
        return result;
    }

    @Override
    public QueryElement getParent()
    {
        return parentElement;
    }

    @Override
    public void setParent(QueryElement parent)
    {
        this.parentElement = parent;
    }

    protected <T extends QueryElement> T prepareChild(T oldElement, T newElement)
    {
        if(oldElement!=null)
            oldElement.setParent(null);
        if(newElement!=null)
            newElement.setParent(this);
        return newElement;
    }
}
