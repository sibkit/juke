package juke.storage;


import juke.querying.queries.Query;
import sibutils.common.Iterator;


/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 19.01.12
 * Time: 15:39
 * To change this juke.sqliteAndroid.templating use File | Settings | File Templates.
 */
public interface StorageIterator extends Iterator<Object[]>
{
    Query getQuery();
}
