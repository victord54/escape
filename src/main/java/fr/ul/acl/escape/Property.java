package fr.ul.acl.escape;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A property is a value that can be subscribed to, to be notified when it changes.
 *
 * @param <T> The type of the value of the property.
 * @author Antoine CONTOUX
 */
public class Property<T> {
    /**
     * The identifier of the property.
     */
    private final String name;
    /**
     * Used to get the value of the property.
     */
    private final Getter<T> getter;
    /**
     * Used to set the value of the property.
     */
    private final Setter<T> setter;
    /**
     * The listeners of the property.
     */
    private final Map<MyPropertyChangeListener<T>, PropertyChangeListener> listeners = new HashMap<>();
    /**
     * The property change support.
     */
    private final PropertyChangeSupport pcs;
    /**
     * The value of the property.
     */
    private T value;
    /**
     * True to log the events of the property.
     */
    private boolean log = false;

    /**
     * @param pcs    The common {@link PropertyChangeSupport PropertyChangeSupport}
     * @param name   The identifier of the property.
     * @param value  The initial value of the property.
     * @param getter The getter of the property. Used to return a value without side effects.
     * @param setter The setter of the property. Used to set a value without side effects.
     * @see Property#Property(T, Getter, Setter)
     * @see Property#Property(PropertyChangeSupport, String, T)
     * @see Property#Property(T)
     */
    public Property(PropertyChangeSupport pcs, String name, T value, Getter<T> getter, Setter<T> setter) {
        this.pcs = pcs == null ? new PropertyChangeSupport(this) : pcs;
        this.name = name == null ? UUID.randomUUID().toString() : name;
        this.value = value;
        this.getter = getter;
        this.setter = setter;
    }

    /**
     * @param value  The initial value of the property.
     * @param getter The getter of the property. Used to return a value without side effects.
     * @param setter The setter of the property. Used to set a value without side effects.
     * @see Property#Property(PropertyChangeSupport, String, T, Getter, Setter)
     * @see Property#Property(PropertyChangeSupport, String, T)
     * @see Property#Property(T)
     */
    public Property(T value, Getter<T> getter, Setter<T> setter) {
        this(null, null, value, getter, setter);
    }

    /**
     * @param pcs   The common {@link PropertyChangeSupport PropertyChangeSupport}
     * @param name  The identifier of the property.
     * @param value The initial value of the property.
     * @see Property#Property(PropertyChangeSupport, String, T, Getter, Setter)
     * @see Property#Property(T, Getter, Setter)
     * @see Property#Property(T)
     */
    public Property(PropertyChangeSupport pcs, String name, T value) {
        this(pcs, name, value, (v) -> v, (v) -> v);
    }

    /**
     * @param value The initial value of the property.
     * @see Property#Property(PropertyChangeSupport, String, T, Getter, Setter)
     * @see Property#Property(T, Getter, Setter)
     * @see Property#Property(PropertyChangeSupport, String, T)
     */
    public Property(T value) {
        this(null, null, value);
    }

    /**
     * @return The value of the property.
     */
    public T get() {
        return getter.get(this.value);
    }

    /**
     * @param value The new value of the property.
     */
    public void set(T value) {
        T newValue = setter.set(value);
        T oldValue = this.value;
        this.value = newValue;
        fire(oldValue);
    }

    /**
     * Add a listener to the property.
     *
     * @param listener The listener to add.
     */
    public void subscribe(MyPropertyChangeListener<T> listener) {
        PropertyChangeListener pcl = (evt) -> listener.propertyChange(evt, (T) evt.getOldValue(), (T) evt.getNewValue());
        pcs.addPropertyChangeListener(name, pcl);
        listeners.put(listener, pcl);

        log("subscribed: " + listener);
    }

    /**
     * Remove a listener from the property.
     *
     * @param listener The listener to remove.
     */
    public void unsubscribe(MyPropertyChangeListener<T> listener) {
        PropertyChangeListener pcl = listeners.remove(listener);
        if (pcl != null) {
            pcs.removePropertyChangeListener(name, pcl);
        }

        log("unsubscribed: " + listener);
    }

    /**
     * Remove all listeners from the property.
     */
    public void unsubscribeAll() {
        listeners.keySet().forEach(this::unsubscribe);
    }

    /**
     * Fire a property change event.
     */
    public void forceFire() {
        fire(null);
    }

    /**
     * @param log True to log the events of the property.
     * @return The property.
     */
    public Property<T> setLog(boolean log) {
        this.log = log;
        return this;
    }

    /**
     * Fire a property change event.
     *
     * @param oldValue The old value of the property.
     */
    private void fire(T oldValue) {
        T newValue = get();
        log("event fired: " + oldValue + " -> " + newValue);
        pcs.firePropertyChange(name, oldValue, newValue);
    }

    /**
     * Log a message.
     *
     * @param message The message to log.
     */
    private void log(String message) {
        if (log) {
            System.out.println("[" + pcs + "] " + name + ": " + message);
        }
    }

    public interface Getter<T> {
        /**
         * Defines how to get the value of the property. Useful to return a value without side effects.
         *
         * @param value The real value of the property.
         * @return The value to return.
         */
        T get(T value);
    }

    public interface Setter<T> {
        /**
         * Defines how to set the value of the property. Useful to set a value without side effects.
         *
         * @param newValue The value to set.
         * @return The new value of the property.
         */
        T set(T newValue);
    }

    public interface MyPropertyChangeListener<T> {
        void propertyChange(PropertyChangeEvent evt, T oldValue, T newValue);
    }
}
