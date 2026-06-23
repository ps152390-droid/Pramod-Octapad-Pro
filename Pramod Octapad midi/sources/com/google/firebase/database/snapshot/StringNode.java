package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.LeafNode;
import com.google.firebase.database.snapshot.Node;

/* JADX INFO: loaded from: classes.dex */
public class StringNode extends LeafNode<StringNode> {
    private final String value;

    public StringNode(String value, Node priority) {
        super(priority);
        this.value = value;
    }

    @Override // com.google.firebase.database.snapshot.Node
    public Object getValue() {
        return this.value;
    }

    /* JADX INFO: renamed from: com.google.firebase.database.snapshot.StringNode$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$firebase$database$snapshot$Node$HashVersion;

        static {
            int[] iArr = new int[Node.HashVersion.values().length];
            $SwitchMap$com$google$firebase$database$snapshot$Node$HashVersion = iArr;
            try {
                iArr[Node.HashVersion.V1.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$firebase$database$snapshot$Node$HashVersion[Node.HashVersion.V2.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    @Override // com.google.firebase.database.snapshot.Node
    public String getHashRepresentation(Node.HashVersion version) {
        switch (AnonymousClass1.$SwitchMap$com$google$firebase$database$snapshot$Node$HashVersion[version.ordinal()]) {
            case 1:
                return getPriorityHash(version) + "string:" + this.value;
            case 2:
                return getPriorityHash(version) + "string:" + Utilities.stringHashV2Representation(this.value);
            default:
                throw new IllegalArgumentException("Invalid hash version for string node: " + version);
        }
    }

    @Override // com.google.firebase.database.snapshot.Node
    public StringNode updatePriority(Node priority) {
        return new StringNode(this.value, priority);
    }

    @Override // com.google.firebase.database.snapshot.LeafNode
    protected LeafNode.LeafType getLeafType() {
        return LeafNode.LeafType.String;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.firebase.database.snapshot.LeafNode
    public int compareLeafValues(StringNode other) {
        return this.value.compareTo(other.value);
    }

    @Override // com.google.firebase.database.snapshot.LeafNode
    public boolean equals(Object other) {
        if (!(other instanceof StringNode)) {
            return false;
        }
        StringNode otherStringNode = (StringNode) other;
        return this.value.equals(otherStringNode.value) && this.priority.equals(otherStringNode.priority);
    }

    @Override // com.google.firebase.database.snapshot.LeafNode
    public int hashCode() {
        return this.value.hashCode() + this.priority.hashCode();
    }
}
