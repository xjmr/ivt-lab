package hu.bme.mit.spaceship;

/**
* A simple spaceship with two proton torpedo stores and four lasers
*/
public class GT4500 implements SpaceShip {

  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  private boolean wasPrimaryFiredLast = false;

  public GT4500() {
    this.primaryTorpedoStore = new TorpedoStore(10);
    this.secondaryTorpedoStore = new TorpedoStore(10);
  }

  public boolean fireLaser(FiringMode firingMode) {
    // TODO not implemented yet
    return false;
  }

  private boolean fire(TorpedoStore store) {
    if(store.isEmpty()) return false;
    boolean success = store.fire(1);
    wasPrimaryFiredLast = (store == primaryTorpedoStore);
    return success;
  }

  /**
  * Tries to fire the torpedo stores of the ship.
  *
  * @param firingMode how many torpedo bays to fire
  * 	SINGLE: fires only one of the bays.
  * 			- For the first time the primary store is fired.
  * 			- To give some cooling time to the torpedo stores, torpedo stores are fired alternating.
  * 			- But if the store next in line is empty, the ship tries to fire the other store.
  * 			- If the fired store reports a failure, the ship does not try to fire the other one.
  * 	ALL:	tries to fire both of the torpedo stores.
  *
  * @return whether at least one torpedo was fired successfully
  */
  @Override
  public boolean fireTorpedo(FiringMode firingMode) {
    if(firingMode == FiringMode.SINGLE) {
      if(wasPrimaryFiredLast) {
          // try to fire the secondary first, but if it fails, try the first
          // note the short circuit semantics of operator||
          return fire(secondaryTorpedoStore) || fire(primaryTorpedoStore);
      } else {
          // try to fire the primary first, then if failed, try the second
          return fire(primaryTorpedoStore) || fire(secondaryTorpedoStore);
      }
    } else if (firingMode == FiringMode.ALL) {
        // try to fire both of the torpedo stores
        return fire(primaryTorpedoStore) && fire(secondaryTorpedoStore);
    }

    // unreachable code but removing it causes compile error
    return false;
  }
}
