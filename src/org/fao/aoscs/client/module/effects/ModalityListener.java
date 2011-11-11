package org.fao.aoscs.client.module.effects;

public interface ModalityListener {
	/** Called when the modality level goes from 0 to 1 ONLY. */
	public abstract void modalityBlurred();
	
	/** Called when the modality level goes from 1 to 0 ONLY. */
	public abstract void modalityClear();
}
