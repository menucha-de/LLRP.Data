package havis.llrpservice.data.message.parameter;

public enum CommunicationsStandard {

	UNSPECIFIED((int) 0), //
	US_FCC_PART_15((int) 1), //
	ETSI_302_208((int) 2), //
	ETSI_300_220((int) 3), //
	AUSTRALIA_LIPD_1W((int) 4), //
	AUSTRALIA_LIPD_4W((int) 5), //
	JAPAN_ARIB_STD_T89((int) 6), //
	HONGKONG_OFTA_1049((int) 7), //
	TAIWAN_DGT_LP0002((int) 8), //
	KOREA_MIC_ARTICLE_5_2((int) 9), //
	MHZ_902_928_4W_FREQ_HOP((int) 10), //
	ETSI_302_208_2W_WITHOUT_LBT((int) 11), //
	BRAZIL_902_907POINT5_MHZ_4W_FREQ_HOP((int) 12), //
	CHINA_840POINT5_844POINT5_MHZ_2W_FREQ_HOP((int) 13), //
	CHINA_920POINT5_924POINT5_MHZ_2W_FREQ_HOP((int) 14), //
	HONGKONG_CHINA_920_925_MHZ_4W((int) 15), //
	ISRAEL_915_917_MHZ((int) 16), //
	JAPAN_952_954_MHZ_4W_LBT((int) 17), //
	JAPAN_952_955_MHZ_20MW_LBT((int) 18), //
	MHZ_865_868_0POINT5W((int) 19), //
	KOREA_REP_917_920POINT8_MHZ_4W_HFSS_OR_LBT((int) 20), //
	KOREA_REP_917_923POINT5_MHZ_200MW_HFSS_OR_LBT((int) 21), //
	MALAYSIA_866_869_MHZ((int) 22), //
	MALAYSIA_919_923_MHZ_2W((int) 23), //
	NEW_ZEALAND_864_868_MHZ_4W((int) 24), //
	SINGAPORE_866_869_MHZ_0POINT5W((int) 25), //
	SINGAPORE_920_925_MHZ_2W((int) 26), //
	SOUTH_AFRICA_915POINT4_919_MHZ_4W_FREQ_HOP((int) 27), //
	SOUTH_AFRICA_919POINT2_921_MHZ_4W_NONMODULATED((int) 28), //
	TAIWAN_922_928_MHZ_1W_FREQ_HOP((int) 29), //
	TAIWAN_922_928_MHZ_0POINT5W_FREQ_HOP((int) 30), //
	THAILAND_920_925_MHZ_4W_FREQ_HOP((int) 31), //
	VENEZULA_922_928_MHZ((int) 32), //
	VIETNAM_866_869_MHZ_0POINT5W((int) 33), //
	VIETNAM_920_925_MHZ_2W((int) 34), //
	RESERVED_FOR_FUTURE_USE((int) 35);

	private final int value;

	private CommunicationsStandard(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CommunicationsStandard get(int value) {
		for (CommunicationsStandard standard : CommunicationsStandard.values()) {
			if (standard.value == value) {
				return standard;
			}
		}
		return null;
	}
}