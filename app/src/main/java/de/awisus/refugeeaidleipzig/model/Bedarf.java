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

/**
 * Created by Jens Awisus on 11.01.16.
 */
public class Bedarf {

    private String name;
    private Kategorie kategorie;
    private int menge;


    public Bedarf(String name, Kategorie kategorie, int menge) {
        this.name = name;
        this.kategorie = kategorie;
        this.menge = menge;
    }


    public String getName() {
        return name;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public int getMenge() {
        return menge;
    }
}
