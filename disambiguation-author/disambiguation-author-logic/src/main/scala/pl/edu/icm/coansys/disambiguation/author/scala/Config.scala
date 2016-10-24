/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.icm.coansys.disambiguation.author.scala

case class Config (
    and_sample: Double = 1.0,
    and_inputDocsData: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_splitted_output_one: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_splitted_output_exh: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_splitted_output_apr_sim: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_splitted_output_apr_no_sim: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/apr-no-sim",
    and_temp_dir: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/temp/",
    and_cid_dockey: String = "cid_dockey",
    and_cid_sname: String = "cid_sname",

    and_aproximate_sim_limit: Int = 1000000,
    and_exhaustive_limit: Int = 6627,

    //			<!-- UDF config -->
    and_skip_empty_features: String = "true",
    and_feature_info: String = "IntersectionPerMaxval#EX_DOC_AUTHS_SNAMES#1.0#1",
    and_lang: String = "all",
    and_statistics: String = "false",
    and_threshold: String = "-0.8",
    and_use_extractor_id_instead_name: String = "true",
    and_outputContribs:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/",
    and_outputContribs_one:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/one",
    and_outputContribs_exh:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/exh",
    and_outputContribs_apr_sim:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr_sim",
    and_outputContribs_apr_no_sim:String= "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs/apr_no_sim",
    and_outputPB: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputPB",
    and_output_unserialized: String = "workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/unserializad"
  
) extends java.io.Serializable
