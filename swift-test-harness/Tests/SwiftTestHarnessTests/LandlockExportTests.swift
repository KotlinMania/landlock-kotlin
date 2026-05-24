import XCTest
import Landlock

final class LandlockExportTests: XCTestCase {
    func testArchitectureConstantsMatchAgnosticBindings() throws {
        XCTAssertEqual(uapi.LANDLOCK_ACCESS_FS_EXECUTE, uapi.i686.LANDLOCK_ACCESS_FS_EXECUTE)
        XCTAssertEqual(uapi.LANDLOCK_ACCESS_NET_BIND_TCP, uapi.i686.LANDLOCK_ACCESS_NET_BIND_TCP)
        XCTAssertEqual(uapi.LANDLOCK_ACCESS_FS_EXECUTE, uapi.x8664.LANDLOCK_ACCESS_FS_EXECUTE)
        XCTAssertEqual(uapi.LANDLOCK_ACCESS_NET_BIND_TCP, uapi.x8664.LANDLOCK_ACCESS_NET_BIND_TCP)
    }

    func testI686LayoutMetadataMatchesBindgenTests() throws {
        XCTAssertTrue(uapi.i686.bindgenTestLayoutLandlockRulesetAttr())
        XCTAssertTrue(uapi.i686.bindgenTestLayoutLandlockPathBeneathAttr())
        XCTAssertTrue(uapi.i686.bindgenTestLayoutLandlockNetPortAttr())

        XCTAssertEqual(24, uapi.i686.LANDLOCK_RULESET_ATTR_SIZE_BYTES)
        XCTAssertEqual(4, uapi.i686.LANDLOCK_RULESET_ATTR_ALIGNMENT_BYTES)
        XCTAssertEqual(16, uapi.i686.LANDLOCK_RULESET_ATTR_SCOPED_OFFSET)

        XCTAssertEqual(12, uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_SIZE_BYTES)
        XCTAssertEqual(1, uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_ALIGNMENT_BYTES)
        XCTAssertEqual(8, uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_PARENT_FD_OFFSET)

        XCTAssertEqual(16, uapi.i686.LANDLOCK_NET_PORT_ATTR_SIZE_BYTES)
        XCTAssertEqual(4, uapi.i686.LANDLOCK_NET_PORT_ATTR_ALIGNMENT_BYTES)
        XCTAssertEqual(8, uapi.i686.LANDLOCK_NET_PORT_ATTR_PORT_OFFSET)
    }

    func testX8664LayoutMetadataMatchesBindgenTests() throws {
        XCTAssertTrue(uapi.x8664.bindgenTestLayoutLandlockRulesetAttr())
        XCTAssertTrue(uapi.x8664.bindgenTestLayoutLandlockPathBeneathAttr())
        XCTAssertTrue(uapi.x8664.bindgenTestLayoutLandlockNetPortAttr())

        XCTAssertEqual(24, uapi.x8664.LANDLOCK_RULESET_ATTR_SIZE_BYTES)
        XCTAssertEqual(8, uapi.x8664.LANDLOCK_RULESET_ATTR_ALIGNMENT_BYTES)
        XCTAssertEqual(16, uapi.x8664.LANDLOCK_RULESET_ATTR_SCOPED_OFFSET)

        XCTAssertEqual(12, uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_SIZE_BYTES)
        XCTAssertEqual(1, uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_ALIGNMENT_BYTES)
        XCTAssertEqual(8, uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_PARENT_FD_OFFSET)

        XCTAssertEqual(16, uapi.x8664.LANDLOCK_NET_PORT_ATTR_SIZE_BYTES)
        XCTAssertEqual(8, uapi.x8664.LANDLOCK_NET_PORT_ATTR_ALIGNMENT_BYTES)
        XCTAssertEqual(8, uapi.x8664.LANDLOCK_NET_PORT_ATTR_PORT_OFFSET)
    }
}
