package hu.bme.mit.spaceship;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class GT4500Test {
  private GT4500 ship;
  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  @Before
  public void init(){
    primaryTorpedoStore = mock(TorpedoStore.class);
    secondaryTorpedoStore = mock(TorpedoStore.class);
    this.ship = new GT4500(primaryTorpedoStore, secondaryTorpedoStore);
  }

  private void setupTorpedoStoreForFire(TorpedoStore ts) {
    when(ts.isEmpty()).thenReturn(false);
    when(ts.fire(1)).thenReturn(true);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    setupTorpedoStoreForFire(primaryTorpedoStore);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    setupTorpedoStoreForFire(primaryTorpedoStore);
    setupTorpedoStoreForFire(secondaryTorpedoStore);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_AllPrimaryFail_Fail(){
    // Arrange
		when(primaryTorpedoStore.isEmpty()).thenReturn(false);
		when(primaryTorpedoStore.fire(1)).thenReturn(false);
    setupTorpedoStoreForFire(secondaryTorpedoStore);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(1);
  }

  @Test
  public void fireTorpedo_AllSecondaryFail_Fail(){
    // Arrange
		setupTorpedoStoreForFire(primaryTorpedoStore);
		when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
		when(secondaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_2SINGLE_Success() {
    // Arrange
    setupTorpedoStoreForFire(primaryTorpedoStore);
    setupTorpedoStoreForFire(secondaryTorpedoStore);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE)
      && ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_3SINGLE_Success() {
    // Arrange
    setupTorpedoStoreForFire(primaryTorpedoStore);
    setupTorpedoStoreForFire(secondaryTorpedoStore);

    // Act
    boolean result = true;
    for(int i = 0; i < 3; ++i) {
      if(!ship.fireTorpedo(FiringMode.SINGLE)) {
        result = false;
        break;
      }
    }

    // Assert
    assertEquals(true, result);
    verify(primaryTorpedoStore, times(2)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_SINGLE_prempty_Success() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);
    setupTorpedoStoreForFire(secondaryTorpedoStore);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primaryTorpedoStore, never()).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_SINGLE_secempty_Success() {
    // Arrange
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);
    setupTorpedoStoreForFire(primaryTorpedoStore);

    // Act
		ship.fireTorpedo(FiringMode.SINGLE); // fire primary store once
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(secondaryTorpedoStore, never()).fire(1);
    verify(primaryTorpedoStore, times(2)).fire(1);
  }

  @Test
  public void fireTorpedo_SINGLE_prfail_Fail() {
    // Arrange
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(false);
    setupTorpedoStoreForFire(secondaryTorpedoStore);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(1);
  }

	@Test
	public void fireLaser_not_implemented() {
		boolean result = ship.fireLaser(FiringMode.SINGLE);
		assertEquals(false, result);
	}
}
