package com.lnotes.grrr.data.dao;

/**
 * Abstract base class for all class-specific Dao implementations.
 * @param <Clazz> is the model type.
 */
public abstract class Dao<Clazz> {

    /**
     * Inserts a single record for the table representing the class specified,
     * (plus any of its dependencies if applicable)
     * and returns the PK id of the item inserted
     */
    public abstract long insert(Clazz insertItem);

    /**
     * Deletes a single record for the table representing the class specified,
     * (plus any of its dependencies if applicable)
     * and returns the PK id of the item deleted.
     */
    public abstract long delete(Clazz deleteItem);
}
