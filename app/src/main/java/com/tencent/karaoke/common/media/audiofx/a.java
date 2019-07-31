package com.tencent.karaoke.common.media.audiofx;


public abstract class a
{
  protected volatile boolean able;
  protected int channels;
  protected int sampleRate;

  public a()
  {
    this.able = true;
    this.sampleRate = 44100;
    this.channels = 2;
  }

  public boolean getEnabled()
  {
    return this.able;
  }

  public void init(int paramInt1, int paramInt2)
  {
    this.sampleRate = paramInt1;
    this.channels = paramInt2;
  }

  public abstract int process(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2);

  public abstract void release();
}