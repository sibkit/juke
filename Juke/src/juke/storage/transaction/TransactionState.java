/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package juke.storage.transaction;

/**
 *
 * @author chelovek
 */
public enum TransactionState
{
    OPENED,
    COMMITTED,
    ABORTED,
    CLOSED
}
