import Testing
import Landlock

@Suite("Landlock Swift Export Tests")
struct LandlockExportTests {
    @Test("Architecture constants match agnostic bindings")
    func architectureConstantsMatchAgnosticBindings() {
        #expect(uapi.LANDLOCK_ACCESS_FS_EXECUTE == uapi.i686.LANDLOCK_ACCESS_FS_EXECUTE)
        #expect(uapi.LANDLOCK_ACCESS_NET_BIND_TCP == uapi.i686.LANDLOCK_ACCESS_NET_BIND_TCP)
        #expect(uapi.LANDLOCK_ACCESS_FS_EXECUTE == uapi.x8664.LANDLOCK_ACCESS_FS_EXECUTE)
        #expect(uapi.LANDLOCK_ACCESS_NET_BIND_TCP == uapi.x8664.LANDLOCK_ACCESS_NET_BIND_TCP)
    }

    @Test("I686 layout metadata matches bindgen tests")
    func i686LayoutMetadataMatchesBindgenTests() {
        #expect(uapi.i686.bindgenTestLayoutLandlockRulesetAttr())
        #expect(uapi.i686.bindgenTestLayoutLandlockPathBeneathAttr())
        #expect(uapi.i686.bindgenTestLayoutLandlockNetPortAttr())

        #expect(uapi.i686.LANDLOCK_RULESET_ATTR_SIZE_BYTES == 24)
        #expect(uapi.i686.LANDLOCK_RULESET_ATTR_ALIGNMENT_BYTES == 4)
        #expect(uapi.i686.LANDLOCK_RULESET_ATTR_SCOPED_OFFSET == 16)

        #expect(uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_SIZE_BYTES == 12)
        #expect(uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_ALIGNMENT_BYTES == 1)
        #expect(uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_PARENT_FD_OFFSET == 8)

        #expect(uapi.i686.LANDLOCK_NET_PORT_ATTR_SIZE_BYTES == 16)
        #expect(uapi.i686.LANDLOCK_NET_PORT_ATTR_ALIGNMENT_BYTES == 4)
        #expect(uapi.i686.LANDLOCK_NET_PORT_ATTR_PORT_OFFSET == 8)
    }

    @Test("X8664 layout metadata matches bindgen tests")
    func x8664LayoutMetadataMatchesBindgenTests() {
        #expect(uapi.x8664.bindgenTestLayoutLandlockRulesetAttr())
        #expect(uapi.x8664.bindgenTestLayoutLandlockPathBeneathAttr())
        #expect(uapi.x8664.bindgenTestLayoutLandlockNetPortAttr())

        #expect(uapi.x8664.LANDLOCK_RULESET_ATTR_SIZE_BYTES == 24)
        #expect(uapi.x8664.LANDLOCK_RULESET_ATTR_ALIGNMENT_BYTES == 8)
        #expect(uapi.x8664.LANDLOCK_RULESET_ATTR_SCOPED_OFFSET == 16)

        #expect(uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_SIZE_BYTES == 12)
        #expect(uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_ALIGNMENT_BYTES == 1)
        #expect(uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_PARENT_FD_OFFSET == 8)

        #expect(uapi.x8664.LANDLOCK_NET_PORT_ATTR_SIZE_BYTES == 16)
        #expect(uapi.x8664.LANDLOCK_NET_PORT_ATTR_ALIGNMENT_BYTES == 8)
        #expect(uapi.x8664.LANDLOCK_NET_PORT_ATTR_PORT_OFFSET == 8)
    }
}
