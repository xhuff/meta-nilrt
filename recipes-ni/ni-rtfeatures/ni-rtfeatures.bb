SUMMARY = "rtfeatures user-space tools"
DESCRIPTION = "Provides user-space tools to support the nirtfeatures kernel module."
HOMEPAGE = "https://github.com/ni/meta-nilrt"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"


DEPENDS += "update-rc.d-native"

PV = "2.0"


SRC_URI += "\
	file://handle_cpld_ip_reset.initd \
	file://ni-rtfeatures.initd \
	file://rtfeatures.rules \
"

S = "${WORKDIR}"


do_install:append () {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${S}/handle_cpld_ip_reset.initd  ${D}${sysconfdir}/init.d/handle_cpld_ip_reset
	install -m 0755 ${S}/ni-rtfeatures.initd         ${D}${sysconfdir}/init.d/ni-rtfeatures

	install -d ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${S}/rtfeatures.rules    ${D}${sysconfdir}/udev/rules.d/rtfeatures.rules
}

pkg_postinst:${PN} () {
	if [ -n "$D" ]; then
		OPT="-r $D"
	else
		OPT=""
	fi
	update-rc.d $OPT handle_cpld_ip_reset start 6 1 3 4 5 .
	update-rc.d $OPT ni-rtfeatures start 20 1 3 4 5 .
}

pkg_postrm:${PN} () {
	if [ -n "$D" ]; then
		OPT="-f -r $D"
	else
		OPT="-f"
	fi
	update-rc.d $OPT handle_cpld_ip_reset remove
	update-rc.d $OPT ni-rtfeatures remove
}


PACKAGE_ARCH = "all"
PACKAGES:remove = "${PN}-staticdev ${PN}-dev ${PN}-dbg"

FILES:${PN} += "\
	${sysconfdir}/init.d/* \
	${sysconfdir}/udev \
"

RDEPENDS:${PN} += "\
	bash \
	ni-netcfgutil \
	udev \
	util-linux \
"
