<?php

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Get All profil
     */
    public function getProfile() {
        $query = mysql_query("SELECT * FROM profil");

        $result = array();

        while($row = mysql_fetch_array($query)){
            array_push($result,array(
                'Dimensional'=>$row['dimensional'],
                'Ht(mm)'=>$row['ht/mm'],
                'Bf(mm)'=>$row['bf/mm'],
                'Tw(mm)'=>$row['tw/mm'],
                'Tf(mm)'=>$row['tf/mm'],
                'R(mm)'=>$row['r/mm'],
                'H1(mm)'=>$row['h1/mm'],
                'H2(mm)'=>$row['h2/mm'],
                'Area_A(cm)'=>$row['sec_of_area_A/cm2'],
                'Unit_Weight'=>$row['unit_weight/kg/m'],
                'GMOI/IX'=>$row['geometrical/ix'],
                'GMOI/IY'=>$row['geometrical/iy'],
                'ROGOA/IX'=>$row['radius/ix'],
                'ROGOA/IY'=>$row['radius/iy'],
                'MOS/SX'=>$row['modulus/sx'],
                'MOS/SY'=>$row['modulus/sy'],
                'MOS/ZX'=>$row['zx']
            ));
        }
        echo json_encode(array('result'=>$result));
    }

    /*
     * get all Baja
     */
    public function getAllBaja(){
        $query = mysql_query("select * from baja");

        $result = array();
        while ($row = mysql_fetch_array($query)){
            array_push($result, array(
                'Jenis_Baja'=>$row['jenis_baja'],
                'FU'=>$row['fu'],
                'FY'=>$row['fy'],
                'Peregangan_Minimum'=>$row['peregangan_min']
            ));
        }
        echo json_encode(array('result'=>$result));
    }


    /*
 * get all Baja
 */
    public function getIwfHwfProfile(){
        $query = mysql_query("SELECT * FROM `profil` Limit 23");

        $result = array();
        while ($row = mysql_fetch_array($query)){
            array_push($result, array(
                'Dimensional'=>$row['dimensional']
            ));
        }
        echo json_encode(array('result'=>$result));
    }


    /*
  * get all mutu Baut
  */
    public function getAllMutuBaut(){
        $query = mysql_query("select * from mutu_baut");

        $result = array();
        while ($row = mysql_fetch_array($query)){
            array_push($result, array(
                'Jenis_Baut'=>$row['jns_baut']
            ));
        }
        echo json_encode(array('result'=>$result));
    }

    public function getJenisPeletakan(){
        $query = mysql_query("select * from jenis_peletakan");

        $result = array();
        while ($row = mysql_fetch_array($query)){
            array_push($result, array(
                'jns_peletakan'=>$row['jns_peletakan']
            ));
        }
        echo json_encode(array('result'=>$result));
    }

    public function getJenisBaut(){
        $query = mysql_query("select * from jenisbaut");

        $result = array();
        while ($row = mysql_fetch_array($query)){
            array_push($result, array(
                'jns_baut'=>$row['jenis_baut']
            ));
        }
        echo json_encode(array('result'=>$result));
    }

    public function getDiameterBaut(){
        $query = mysql_query("select * from diameterbaut");

        $result = array();
        while ($row = mysql_fetch_array($query)){
            array_push($result, array(
                'diameter_baut'=>$row['diameter_baut']
            ));
        }
        echo json_encode(array('result'=>$result));
    }

 /*
 * get all Baja
 */
    public function getBajaBalokLentur(){
        $query = mysql_query("select * from baja");

        $result = array();
        while ($row = mysql_fetch_array($query)){
            array_push($result, array(
                'Jenis_Baja'=>$row['jenis_baja'],
                'FY'=>$row['fy'],
            ));
        }
        echo json_encode(array('result'=>$result));
    }
	

    /*
     * Select data by Dimensi
     */
    public function selectProfileLenturBalok($dimensional){
        $result = mysql_query("SELECT * FROM profil WHERE dimensional = '$dimensional'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($dimensional) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /*
     * Select data by Dimensi
     */
    public function selectProfile($dimensional){
        $result = mysql_query("SELECT * FROM profil WHERE dimensional = '$dimensional'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($dimensional) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }
	
	public function selectRnt(){
        $result = mysql_query("SELECT rnt FROM `tb_rnt` Order by id DESC") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            //if ($dimensional) {
                // user authentication details are correct
                return $result;
            //}
        } else {
            // user not found
            return false;
        }
    }

    /*
     * Select data by Dimensi
     */
    public function selectJenisPeletakan($jnsPeletakan){
        $result = mysql_query("SELECT * FROM jenis_peletakan WHERE jns_peletakan = '$jnsPeletakan'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($jnsPeletakan) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /*
     * Select data by Dimensi
     */
    public function selectProfileLenturBaja($jenisbaja){
        $result = mysql_query("SELECT * FROM baja WHERE jenis_baja = '$jenisbaja'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($jenisbaja) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /*
     * Select data by Dimensi
     */
    public function selectMutuBaut($jenisbaut){
        $result = mysql_query("SELECT * FROM mutu_baut WHERE jns_baut = '$jenisbaut'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($jenisbaut) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }
	
	public function selectDiameterBautGayaTarik($diamterbaut){
        $result = mysql_query("SELECT * FROM diameterbaut WHERE diameter_baut = '$diamterbaut'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($diamterbaut) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    public function selectJenisBaut($jenisbaut){
        $result = mysql_query("SELECT * FROM jenisbaut WHERE jenis_baut = '$jenisbaut'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            if ($jenisbaut) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /*
     * Insert Absen Mahasiswa
     */
    public function inputRnt($rnt){
        $result  = mysql_query("INSERT INTO tb_rnt(rnt) VALUES ('$rnt')");
        if($result){
            $id = mysql_insert_id();
            $result = mysql_query("SELECT * FROM tb_rnt WHERE id = $id");
            return mysql_fetch_array($result);
        }else{
            return false;
        }
    }

}
?>
