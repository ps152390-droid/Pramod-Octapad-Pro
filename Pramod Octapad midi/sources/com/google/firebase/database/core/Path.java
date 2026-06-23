package com.google.firebase.database.core;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
public class Path implements Iterable<ChildKey>, Comparable<Path> {
    private static final Path EMPTY_PATH = new Path("");
    private final int end;
    private final ChildKey[] pieces;
    private final int start;

    public static Path getRelative(Path from, Path to) {
        ChildKey outerFront = from.getFront();
        ChildKey innerFront = to.getFront();
        if (outerFront == null) {
            return to;
        }
        if (outerFront.equals(innerFront)) {
            return getRelative(from.popFront(), to.popFront());
        }
        throw new DatabaseException("INTERNAL ERROR: " + to + " is not contained in " + from);
    }

    public static Path getEmptyPath() {
        return EMPTY_PATH;
    }

    public Path(ChildKey... segments) {
        this.pieces = (ChildKey[]) Arrays.copyOf(segments, segments.length);
        this.start = 0;
        this.end = segments.length;
        int length = segments.length;
        for (int i = 0; i < length; i++) {
            ChildKey name = segments[i];
            Utilities.hardAssert(name != null, "Can't construct a path with a null value!");
        }
    }

    public Path(List<String> segments) {
        this.pieces = new ChildKey[segments.size()];
        int i = 0;
        for (String segment : segments) {
            this.pieces[i] = ChildKey.fromString(segment);
            i++;
        }
        this.start = 0;
        this.end = segments.size();
    }

    public Path(String pathString) {
        String[] segments = pathString.split("/", -1);
        int count = 0;
        for (String str : segments) {
            if (str.length() > 0) {
                count++;
            }
        }
        this.pieces = new ChildKey[count];
        int j = 0;
        for (String segment : segments) {
            if (segment.length() > 0) {
                this.pieces[j] = ChildKey.fromString(segment);
                j++;
            }
        }
        this.start = 0;
        this.end = this.pieces.length;
    }

    private Path(ChildKey[] pieces, int start, int end) {
        this.pieces = pieces;
        this.start = start;
        this.end = end;
    }

    public Path child(Path path) {
        int newSize = size() + path.size();
        ChildKey[] newPieces = new ChildKey[newSize];
        System.arraycopy(this.pieces, this.start, newPieces, 0, size());
        System.arraycopy(path.pieces, path.start, newPieces, size(), path.size());
        return new Path(newPieces, 0, newSize);
    }

    public Path child(ChildKey child) {
        int size = size();
        ChildKey[] newPieces = new ChildKey[size + 1];
        System.arraycopy(this.pieces, this.start, newPieces, 0, size);
        newPieces[size] = child;
        return new Path(newPieces, 0, size + 1);
    }

    public String toString() {
        if (isEmpty()) {
            return "/";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = this.start; i < this.end; i++) {
            builder.append("/");
            builder.append(this.pieces[i].asString());
        }
        return builder.toString();
    }

    public String wireFormat() {
        if (isEmpty()) {
            return "/";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = this.start; i < this.end; i++) {
            if (i > this.start) {
                builder.append("/");
            }
            builder.append(this.pieces[i].asString());
        }
        return builder.toString();
    }

    public List<String> asList() {
        List<String> result = new ArrayList<>(size());
        for (ChildKey key : this) {
            result.add(key.asString());
        }
        return result;
    }

    public ChildKey getFront() {
        if (isEmpty()) {
            return null;
        }
        return this.pieces[this.start];
    }

    public Path popFront() {
        int newStart = this.start;
        if (!isEmpty()) {
            newStart++;
        }
        return new Path(this.pieces, newStart, this.end);
    }

    public Path getParent() {
        if (isEmpty()) {
            return null;
        }
        return new Path(this.pieces, this.start, this.end - 1);
    }

    public ChildKey getBack() {
        if (!isEmpty()) {
            return this.pieces[this.end - 1];
        }
        return null;
    }

    public boolean isEmpty() {
        return this.start >= this.end;
    }

    public int size() {
        return this.end - this.start;
    }

    @Override // java.lang.Iterable
    public Iterator<ChildKey> iterator() {
        return new Iterator<ChildKey>() { // from class: com.google.firebase.database.core.Path.1
            int offset;

            {
                this.offset = Path.this.start;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.offset < Path.this.end;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public ChildKey next() {
                if (hasNext()) {
                    ChildKey[] childKeyArr = Path.this.pieces;
                    int i = this.offset;
                    ChildKey child = childKeyArr[i];
                    this.offset = i + 1;
                    return child;
                }
                throw new NoSuchElementException("No more elements.");
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Can't remove component from immutable Path!");
            }
        };
    }

    public boolean contains(Path other) {
        if (size() > other.size()) {
            return false;
        }
        int i = this.start;
        int j = other.start;
        while (i < this.end) {
            if (!this.pieces[i].equals(other.pieces[j])) {
                return false;
            }
            i++;
            j++;
        }
        return true;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Path)) {
            return false;
        }
        if (this == other) {
            return true;
        }
        Path otherPath = (Path) other;
        if (size() != otherPath.size()) {
            return false;
        }
        int i = this.start;
        for (int j = otherPath.start; i < this.end && j < otherPath.end; j++) {
            if (!this.pieces[i].equals(otherPath.pieces[j])) {
                return false;
            }
            i++;
        }
        return true;
    }

    public int hashCode() {
        int hashCode = 0;
        for (int i = this.start; i < this.end; i++) {
            hashCode = (hashCode * 37) + this.pieces[i].hashCode();
        }
        return hashCode;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0020, code lost:
    
        if (r0 != r2) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0024, code lost:
    
        if (r1 != r5.end) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0026, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0028, code lost:
    
        if (r0 != r2) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x002a, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x002c, code lost:
    
        return 1;
     */
    @Override // java.lang.Comparable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int compareTo(com.google.firebase.database.core.Path r5) {
        /*
            r4 = this;
            int r0 = r4.start
            int r1 = r5.start
        L4:
            int r2 = r4.end
            if (r0 >= r2) goto L20
            int r3 = r5.end
            if (r1 >= r3) goto L20
            com.google.firebase.database.snapshot.ChildKey[] r2 = r4.pieces
            r2 = r2[r0]
            com.google.firebase.database.snapshot.ChildKey[] r3 = r5.pieces
            r3 = r3[r1]
            int r2 = r2.compareTo(r3)
            if (r2 == 0) goto L1b
            return r2
        L1b:
            int r0 = r0 + 1
            int r1 = r1 + 1
            goto L4
        L20:
            if (r0 != r2) goto L28
            int r3 = r5.end
            if (r1 != r3) goto L28
            r2 = 0
            return r2
        L28:
            if (r0 != r2) goto L2c
            r2 = -1
            return r2
        L2c:
            r2 = 1
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.database.core.Path.compareTo(com.google.firebase.database.core.Path):int");
    }
}
