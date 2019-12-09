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
package be.mdi.testing.qc.model.entities;

import be.mdi.testing.qc.model.QcType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Entities")
public class QcDefects extends QcEntities implements QcEntitiesInterface<QcDefect, QcDefects> {

    @XmlElement(name = "Entity")
    protected List<QcDefect> entities;

    public QcDefects()  {
        super(QcType.DEFECT);
        entities = new ArrayList<QcDefect>();
    }

    public QcDefect get(int index) {
        QcDefect d = entities.get(index);
        d.setDomain(getDomain());
        d.setProject(getProject());
        return d;
    }

    public QcDefects add(QcDefect d) {
        entities.add(d);
        return this;
    }
}
