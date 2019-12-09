/*
 * QC REST API client
 *
 * Copyright (C) 2019  matthias.dirickx@outlook.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package be.mdi.testing.qc.model.fields;

public enum QcAttachmentField {

    REF_SUBTYPE("ref-subtype"),
    VC_USER_NAME("vc-user-name"),
    ID("id"),
    PARENT_ID("parent-id"),
    LAST_MODIFIED("last-modified"),
    REF_CKOUT_PATH("ref-ckout-path"),
    DESCRIPTION("description"),
    VC_CUR_VER("vc-cur-ver"),
    NAME("name"),
    REF_TYPE("ref-type"),
    PARENT_TYPE("parent-type"),
    FILE_SIZE("file-size");

    private String name;

    QcAttachmentField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
