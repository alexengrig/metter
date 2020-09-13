package dev.alexengrig.metter.demo;

public class ManualPersonMetadataHolderTest extends PersonMetadataHolderTester {
    @Override
    protected PersonMetadataHolder getMetadataHolder() {
        return new ManualPersonMetadataHolder();
    }
}