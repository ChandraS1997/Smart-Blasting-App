package com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class Response3DTable18PreSpilitDataModel implements Serializable {

	@SerializedName("LinePoints")
	private String linePointsStr;

	private List<LinePointsItem> linePoints;

	@SerializedName("HoleDiameter")
	private String holeDiameter;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("CombineBlastNo")
	private String combineBlastNo;

	@SerializedName("HoleDetail")
	private String holeDetailStr;

	private List<HoleDetailItem> holeDetail;

	@SerializedName("InitHoleId")
	private String initHoleId;

	@SerializedName("HoleDepth")
	private String holeDepth;

	@SerializedName("Spacing")
	private String spacing;

	@SerializedName("HolePoints")
	private String holePointsStr;

	private List<HolePointsItem> holePoints;

	@SerializedName("Delay")
	private String delay;

	public void setLinePoints(List<LinePointsItem> linePoints){
		this.linePoints = linePoints;
	}

	public List<LinePointsItem> getLinePoints(){
		return linePoints;
	}

	public void setHoleDiameter(String holeDiameter){
		this.holeDiameter = holeDiameter;
	}

	public String getHoleDiameter(){
		return holeDiameter;
	}

	public void setDesignId(String designId){
		this.designId = designId;
	}

	public String getDesignId(){
		return designId;
	}

	public void setCombineBlastNo(String combineBlastNo){
		this.combineBlastNo = combineBlastNo;
	}

	public String getCombineBlastNo(){
		return combineBlastNo;
	}

	public void setHoleDetail(List<HoleDetailItem> holeDetail){
		this.holeDetail = holeDetail;
	}

	public List<HoleDetailItem> getHoleDetail(){
		return holeDetail;
	}

	public void setInitHoleId(String initHoleId){
		this.initHoleId = initHoleId;
	}

	public String getInitHoleId(){
		return initHoleId;
	}

	public void setHoleDepth(String holeDepth){
		this.holeDepth = holeDepth;
	}

	public String getHoleDepth(){
		return holeDepth;
	}

	public void setSpacing(String spacing){
		this.spacing = spacing;
	}

	public String getSpacing(){
		return spacing;
	}

	public void setHolePoints(List<HolePointsItem> holePoints){
		this.holePoints = holePoints;
	}

	public List<HolePointsItem> getHolePoints(){
		return holePoints;
	}

	public void setDelay(String delay){
		this.delay = delay;
	}

	public String getDelay(){
		return delay;
	}

	public String getLinePointsStr() {
		return linePointsStr;
	}

	public void setLinePointsStr(String linePointsStr) {
		this.linePointsStr = linePointsStr;
		linePoints = new Gson().fromJson(linePointsStr, new TypeToken<List<LinePointsItem>>(){}.getType());
	}

	public String getHoleDetailStr() {
		return holeDetailStr;
	}

	public void setHoleDetailStr(String holeDetailStr) {
		this.holeDetailStr = holeDetailStr;
		List<HoleDetailItem> holeDetailItemList = new Gson().fromJson(holeDetailStr, new TypeToken<List<HoleDetailItem>>(){}.getType());
		holeDetail = new ArrayList<>();
		for (HoleDetailItem item : holeDetailItemList) {
			item.setDesignId(getDesignId());
			holeDetail.add(item);
		}
	}

	public String getHolePointsStr() {
		return holePointsStr;
	}

	public void setHolePointsStr(String holePointsStr) {
		this.holePointsStr = holePointsStr;
		holePoints = new Gson().fromJson(holePointsStr, new TypeToken<List<HolePointsItem>>(){}.getType());
	}
}