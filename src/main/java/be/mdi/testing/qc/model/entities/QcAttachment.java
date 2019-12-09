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
 *s
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package be.mdi.testing.qc.model.entities;

import be.mdi.testing.qc.model.QcType;
import be.mdi.testing.qc.model.fields.QcAttachmentField;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.InputStream;

@XmlRootElement(name = "Entity")
public class QcAttachment extends QcEntity {

    @XmlTransient
    private QcType parentType;
    @XmlTransient
    private QcType parentOfParentType;
    @XmlTransient
    private String parentIdentifier;
    @XmlTransient
    private String parentOfParentIdentifier;


    private InputStream attachmentInputStream;

    public QcAttachment() {
        super(QcType.ATTACHMENT);
    }

    @Override
    public String getUrl() {
        String url =  "rest/domains/" + getDomain() + "/projects/" + getProject() + "/";

        if(parentType.hasTypeParent()) {
            url += parentOfParentType.getRestUrlType();
            url += "/" + parentOfParentIdentifier + "/";
        }

        url += parentType.getRestUrlType();
        url += "/" + parentIdentifier;
        url += "/" + getQcType().getRestUrlType();

        if(getFields().containsKey("id") && getFields().get("id") != null) {
            url += "/" + getFields().get("id");
        }

        return url;
    }

    public String getField(QcAttachmentField field) {
        return fields.get(field.getName());
    }

    public QcAttachment setField(QcAttachmentField field, String value) {
        fields.put(field.getName(), value);
        return this;
    }

    public InputStream getAttachmentInputStream() { return attachmentInputStream; }

    @XmlTransient
    public void setAttachmentInputStream(InputStream attachmentInputStream) {
        this.attachmentInputStream = attachmentInputStream;
    }

    public QcAttachment setParent(QcType parentType, String parentIdentifier) {
        this.parentType = parentType;
        this.parentIdentifier = parentIdentifier;
        return this;
    }

    public QcAttachment setParent(QcType parentType,
                                  String parentIdentifier,
                                  QcType parentOfParentType,
                                  String parentOfParentIdentifier) {
        this.parentType = parentType;
        this.parentIdentifier = parentIdentifier;
        this.parentOfParentType = parentOfParentType;
        this.parentOfParentIdentifier = parentOfParentIdentifier;
        return this;
    }

    public boolean hasFile() {
        return !(attachmentInputStream == null);
    }
}
