package com.stratio.mojo.scala.crossbuild;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class ReplacingInputStream extends InputStream {

  private final InputStream input;
  private final List<Replacement> occurrences;
  private Replacement currentOccurrence;
  private int currentOccurrenceIndex;
  private long inputOffset;
  private int innerOffset;
  private boolean replacing;

  public ReplacingInputStream(final InputStream input, final List<Replacement> occurrences) {
    this.input = input;
    this.occurrences = new ArrayList<>(occurrences);
    this.currentOccurrenceIndex = 0;
    this.currentOccurrence = (occurrences.isEmpty())? null : this.occurrences.get(this.currentOccurrenceIndex);
    this.inputOffset = 0;
    this.innerOffset = -1;
    this.replacing = false;
  }

  @Override public int read() throws IOException {
    int toRead;
    if (currentOccurrence != null) {
      if (currentOccurrence.offset == inputOffset || replacing) {

        replacing = true;

        /* During first iteration, we skip bytes from the underlying input. */
        if (innerOffset == -1) {
          for (int i = 0; i < currentOccurrence.length; i++) {
            this.inputOffset++;
            this.input.read();
          }
        }

        /* Increment inner offset and read byte from replacement */
        innerOffset++;
        toRead = currentOccurrence.replacement[innerOffset];

        /* If we are finished replacing, reset variables */
        if (innerOffset == currentOccurrence.length - 1) {
          replacing = false;
          innerOffset = -1;
          currentOccurrenceIndex++;
          if (currentOccurrenceIndex < occurrences.size()) {
            currentOccurrence = occurrences.get(currentOccurrenceIndex);
          }
        }

        //inputOffset++;
        return toRead;
      }
      toRead = this.input.read();
      inputOffset++;
      return toRead;
    }
    return 0;
  }

  @Override public void close() throws IOException {
    super.close();
    this.input.close();
  }

}
