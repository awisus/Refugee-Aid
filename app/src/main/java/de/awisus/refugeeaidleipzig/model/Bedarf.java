/*
 * Copyright 2016 Jens Awisus <awisus.gdev@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package de.awisus.refugeeaidleipzig.model;

import java.util.LinkedList;

/**
 * Created by on 11.01.16.
 *
 * @author Jens Awisus
 */
public class Bedarf implements Comparable<Bedarf> {

    private int id;
    private LinkedList<String> pfad;


    public Bedarf() {
        this.pfad = new LinkedList<>();
    }

    public void put(int id, String name) {
        this.id = id;
        this.pfad.add(name);
    }

    @Override
    public int compareTo(Bedarf another) {
        return -another.toString().compareTo(toString());
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String str = pfad.get(0);
        for(int i = 1; i < pfad.size(); i++) {
            str += " > " + pfad.get(i);
        }
        return str;
    }
}
