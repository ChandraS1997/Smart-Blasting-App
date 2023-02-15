package com.smart_blasting_drilling.android.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class ResponseFileDetailsTableEntity implements Serializable {

	@PrimaryKey(autoGenerate = true)
	public long id;

	@ColumnInfo(name = "BenchData")
	private String data;

	public ResponseFileDetailsTableEntity() {
	}

	public ResponseFileDetailsTableEntity(long id, String data) {
		this.id = id;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}