package com.example.addon.Utils;

public class AAABLYA {

    private long nanoTime = -1L;

    public void reset() {
        nanoTime = System.nanoTime();
    }

    public void setTicks(long ticks) { nanoTime = System.nanoTime() - convertTicksToNano(ticks); }
    public void setMillis(long time) { nanoTime = System.nanoTime() - convertMillisToNano(time); }
    public void setSec(long time) { nanoTime = System.nanoTime() - convertSecToNano(time); }

    public long getTicks() { return convertNanoToTicks(nanoTime); }
    public long getMillis() { return convertNanoToMillis(nanoTime); }
    public long getSec() { return convertNanoToSec(nanoTime); }

    public boolean passedTicks(long ticks) { return passedNano(convertTicksToNano(ticks)); }
    public boolean passedNano(long time) { return System.nanoTime() - nanoTime >= time; }
    public boolean passedMillis(long time) { return passedNano(convertMillisToNano(time)); }
    public boolean passedSec(long time) { return passedNano(convertSecToNano(time)); }

    public long convertMillisToTicks(long time) { return time / 50; }
    public long convertTicksToMillis(long ticks) { return ticks * 50; }
    public long convertNanoToTicks(long time) { return convertMillisToTicks(convertNanoToMillis(time)); }
    public long convertTicksToNano(long ticks) { return convertMillisToNano(convertTicksToMillis(ticks)); }

    public long convertSecToMillis(long time) { return time * 1000L; }
    public long convertSecToMicro(long time) { return convertMillisToMicro(convertSecToMillis(time)); }
    public long convertSecToNano(long time) { return convertMicroToNano(convertMillisToMicro(convertSecToMillis(time))); }

    public long convertMillisToMicro(long time) { return time * 1000L; }
    public long convertMillisToNano(long time) { return convertMicroToNano(convertMillisToMicro(time)); }

    public long convertMicroToNano(long time) { return time * 1000L; }

    public long convertNanoToMicro(long time) { return time / 1000L; }
    public long convertNanoToMillis(long time) { return convertMicroToMillis(convertNanoToMicro(time)); }
    public long convertNanoToSec(long time) { return convertMillisToSec(convertMicroToMillis(convertNanoToMicro(time))); }

    public long convertMicroToMillis(long time) { return time / 1000L; }
    public long convertMicroToSec(long time) { return convertMillisToSec(convertMicroToMillis(time)); }

    public long convertMillisToSec(long time) { return time / 1000L; }
}
