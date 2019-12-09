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

import be.mdi.testing.qc.model.entities.QcAttachment;
import be.mdi.testing.qc.model.entities.QcAttachments;
import be.mdi.testing.qc.model.fields.QcAttachmentField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QcAttachmentsTest {
    @Test
    public void qcAttachmentsHaveMultipleAttachmentsTest() {
        QcAttachments qcAttachments = new QcAttachments();

        qcAttachments.add(
                new QcAttachment().setField(QcAttachmentField.NAME, "theName")
            );
        qcAttachments.add(
                new QcAttachment().setField(QcAttachmentField.NAME, "secondName")
            );

        Assertions.assertEquals("theName", qcAttachments.get(0).getField((QcAttachmentField.NAME)));
        Assertions.assertEquals("secondName", qcAttachments.get(1).getField((QcAttachmentField.NAME)));
    }
}
