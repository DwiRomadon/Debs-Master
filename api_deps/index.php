<?php

/**
 * File to handle all API requests
 * Accepts GET and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data

  /**
 * check for POST request 
 */
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];

    // include db handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

    // response Array
    $response = array("tag" => $tag, "error" => FALSE);

    // check for tag type
    if ($tag == 'getProfile') {

        // check for user
        $user = $db->getProfile();

    }else if ($tag == 'getJenisPeletakan'){

        $user = $db->getJenisPeletakan();

    }else if ($tag == 'getProfilIwfHwf'){

        $user = $db->getIwfHwfProfile();

    }
    else if ($tag == 'getJenisBaut'){

        $user = $db->getJenisBaut();

    }else if ($tag == 'getAllMutuBaut'){

        $user = $db->getAllMutuBaut();

    }else if ($tag == 'getDiameterBaut'){

        $user = $db->getDiameterBaut();

    } else if ($tag == 'getBaja') {

        // check for user
        $user = $db->getAllBaja();

    }else if ($tag == 'getBajaBalokLentur') {

        // check for user
        $user = $db->getBajaBalokLentur();

    }else if($tag == 'selectDimensionalBalokLentur'){
        $dimensional = $_POST['dimensional'];

        $user = $db->selectProfileLenturBalok($dimensional);
        if ($user) {
            // user stored successfully
            $response["error"]               = FALSE;
            $response["Ht"]                  = $user["ht/mm"];
            $response["Bf"]                  = $user["bf/mm"];
            $response["Tf"]                  = $user["tf/mm"];
            $response["H2"]                  = $user["h2/mm"];
            $response["Tw"]                  = $user["tw/mm"];
            $response["Zx"]                  = $user["zx"];
            $response["R"]                   = $user["radius/iy"];
            $response["A"]                   = $user["sec_of_area_A/cm2"];
            $response["X1"]                  = $user["x1"];
            $response["X2"]                  = $user["x2"];
            $response["SX"]                  = $user["modulus/sx"];
            $response["Unit_Weight"]         = $user["unit_weight/kg/m"];
            $response["Geomatrical_IX"]      = $user["geometrical/ix"];
            $response["Geomatrical_IY"]      = $user["geometrical/iy"];
            $response["Radius_IX"]           = $user["radius/ix"];
            $response["Radius_IY"]           = $user["radius/iy"];
            $response["SY"]                  = $user["modulus/sy"];
            $response["H1"]                  = $user["h1/mm"];
            $response["success"]             = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal, ";
            echo json_encode($response);
        }
    }else if($tag == 'selectInfoProfil'){
        $dimensional = $_POST['dimensional'];

        $user = $db->selectProfile($dimensional);
        if ($user) {
            // user stored successfully
            $response["error"]               = FALSE;
            $response["Ht"]                  = $user["ht/mm"];
            $response["Bf"]                  = $user["bf/mm"];
            $response["Tf"]                  = $user["tf/mm"];
            $response["H2"]                  = $user["h2/mm"];
            $response["Tw"]                  = $user["tw/mm"];
            $response["Zx"]                  = $user["zx"];
            $response["R"]                   = $user["r/mm"];
            $response["A"]                   = $user["sec_of_area_A/cm2"];
            $response["X1"]                  = $user["x1"];
            $response["X2"]                  = $user["x2"];
            $response["SX"]                  = $user["modulus/sx"];
            $response["Unit_Weight"]         = $user["unit_weight/kg/m"];
            $response["Geomatrical_IX"]      = $user["geometrical/ix"];
            $response["Geomatrical_IY"]      = $user["geometrical/iy"];
            $response["Radius_IX"]           = $user["radius/ix"];
            $response["Radius_IY"]           = $user["radius/iy"];
            $response["SY"]                  = $user["modulus/sy"];
            $response["H1"]                  = $user["h1/mm"];
            $response["success"]             = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal, ";
            echo json_encode($response);
        }
    } else if($tag == 'selectJenisBajaBalokLentur'){
        $jenisbaja = $_POST['jenisbaja'];

        $user = $db->selectProfileLenturBaja($jenisbaja);
        if ($user) {
            // user stored successfully
            $response["error"]                      = FALSE;
            $response["FY"]                         = $user["fy"];
            $response["Fu"]                         = $user["fu"];
            $response["Peregangan_Min"]             = $user["peregangan_min"];
            $response["success"]                    = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal,";
            echo json_encode($response);
        }
    }else if($tag == 'selectMutuBaut'){
        $jenisbaut = $_POST['jenisbaut'];

        $user = $db->selectMutuBaut($jenisbaut);
        if ($user) {
            // user stored successfully
            $response["error"]                      = FALSE;
            $response["Kekuatan"]                   = $user["kekuatan_tarik"];
            $response["success"]                    = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal wah";
            echo json_encode($response);
        }
    }
    else if($tag == 'selectDiameterBaut'){
        $diameterBaut = $_POST['diameterBaut'];

        $user = $db->selectDiameterBautGayaTarik($diameterBaut);
        if ($user) {
            // user stored successfully
            $response["error"]                      = FALSE;
            $response["DiameterBaut"]               = $user["diameter_baut"];
            $response["GayaTarik"]                  = $user["gaya_tarik"];
            $response["success"]                    = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal,";
            echo json_encode($response);
        }
    }
	else if($tag == 'selectJenisBaut'){
        $jenisbaja = $_POST['jenisbaut'];

        $user = $db->selectJenisBaut($jenisbaja);
        if ($user) {
            // user stored successfully
            $response["error"]                      = FALSE;
            $response["Faktor_Keamanan"]            = $user["faktor_keamanan"];
            $response["success"]                    = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal Baut,";
            echo json_encode($response);
        }
    } else if($tag == 'selectJenisPeletakan'){
        $jenisPeletakan = $_POST['jenisPeletakan'];

        $user = $db->selectJenisPeletakan($jenisPeletakan);
        if ($user) {
            // user stored successfully
            $response["error"]                      = FALSE;
            $response["Kc"]                         = $user["kc"];
            $response["success"]                    = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal !!";
            echo json_encode($response);
        }
    }else if($tag == 'selectRnt'){

        $user = $db->selectRnt();
        if ($user) {
            // user stored successfully
            $response["error"]                      = FALSE;
            $response["Rnt"]                        = $user["rnt"];
            $response["success"]                    = "Sukses Mengambil Data ";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"]      = TRUE;
            $response["error_msg"]  = "Gagal !!";
            echo json_encode($response);
        }
    }else if($tag == 'insertRnt'){
        $rnt  = $_POST['rnt'];

        $user = $db->inputRnt($rnt);

        if($user){
            $response["error"]    = FALSE;
            $response["Id"]       = $user["id"];
            $response["Rnt"]      = $user["rnt"];
            $response["success"]  = "Sukses Input Rnt ";
            echo json_encode($response);
        }else{
            $response["error"]     = TRUE;
            $response["error_msg"] = "Gagal !!";
            echo json_encode($response);
        }
    } else {
        // user failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknow 'tag' value.";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameter 'tag' is missing!";
    echo json_encode($response);
}
?>
