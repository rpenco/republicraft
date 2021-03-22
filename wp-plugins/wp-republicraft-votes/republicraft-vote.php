<?php
/*
Plugin Name: Républicraft Votes
Plugin URI: http://republicraft.fr/votes/
Description: Républicraft Votes display votes for users in a table with page shortcode: "[republicraft-votes]"
Author: rpenco
Version: 0.0.1
Author URI: https://republicraft.fr
*/

define( 'PLUGIN_WITH_CLASSES__FILE__', __FILE__ );
require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );

function player_vote(){
    try {
        $mydb = new wpdb('root','9F00DC68980C382F49431CD22AB5FE2B2CD87C00','republicraft','mariadb');
        $tablename = "votes"; 
        $mydb->insert( 
            $tablename, 
            array( 
                'date' => current_time( 'mysql' ), 
                'player_username' => 'myusername', 
                'provider' => 'serveur-prive.net', 
                'status' => 'voted', 
            ) 
        );    
    } catch (Exception $e) {
        error_log($e);
    }
}

/**
 * Called when plugin initialize
 */
function create_plugin_database_table(){

}

 // Add Shortcode
function votes_shortcode( $atts , $content = null ) {

    try {
        $mydb = new wpdb('root','9F39DC68980C382F49431CD22AB5FE2B2CD87C79','republicraft','mariadb');
        $tablename = "votes"; 
        $myrows = $mydb->get_results( "
            SELECT COUNT(id) as votes, player_username, CONVERT_TZ(vote_date,'SYSTEM','Europe/Paris') as last_date
            FROM ".$tablename."
            WHERE status = 'voted' 
            AND MONTH(CONVERT_TZ(vote_date,'SYSTEM','Europe/Paris')) = MONTH(NOW())
            AND YEAR(CONVERT_TZ(vote_date,'SYSTEM','Europe/Paris')) = YEAR(NOW()) 
            GROUP BY player_username 
            ORDER BY votes DESC, last_date ASC
        ");

        $output = '<h2>Top votes du mois</h2>';
        $output .= '<table>';
        $output .= '<tr><th>Position</th><th colspan="2">Joueur</th><th>Votes</th></tr>';
        foreach($myrows as $pos => $vote){
            $output .= '<tr><td width="50px">#'.($pos+1).'</td><td width="50px"><img src="https://minotar.net/bust/'.$vote->player_username.'.png" width="50px"/></td><td>'.$vote->player_username.'</td><td width="50px">'.$vote->votes.' votes</td></tr>';
        }
        $output .= '</table>';

    } catch (Exception $e) {
        error_log($e);
    }
	
	// Reset post data
	// wp_reset_postdata();
	
	// Return code
	return $output;

}




function test_plugin_setup_menu(){
    add_menu_page( 'Républicraft votes Page', 'Républicraft votes', 'manage_options', 'republicraft-votes-plugin', 'test_init' );
}

function test_init(){
    echo "<h1>Républicraft Votes</h1>";
    try {
        $mydb = new wpdb('root','9F00DC68980C382F49431CD22AB5FE2B2CD87C00','republicraft','mariadb');
        $tablename = "votes";


        $myrows = $mydb->get_results( "
            SELECT COUNT(id) as votes, player_username, status  
            FROM ".$tablename."
            WHERE status = 'voted' 
            AND MONTH(vote_date) = MONTH(CURRENT_DATE())
            AND YEAR(vote_date) = YEAR(CURRENT_DATE()) 
            GROUP BY player_username 
            ORDER BY votes DESC
        ");

        echo '<h2>Top votes du mois</h2>';
        echo '<table>';
        echo '<tr><th>Position</th><th colspan="2">Joueur</th><th>Votes</th></tr>';
        foreach($myrows as $pos => $vote){
            echo '<tr><td width="50px">#'.($pos+1).'</td><td width="50px"><img src="https://minotar.net/bust/'.$vote->player_username.'.png" width="50px"/></td><td>'.$vote->player_username.'</td><td width="50px">'.$vote->votes.' votes</td></tr>';
        }
        echo '</table>';


        $tablename = "players";


        $myrows = $mydb->get_results( "
            SELECT username, balance, ban, created_at 
            FROM ".$tablename."
            WHERE DAY(created_at) = DAY(CURRENT_DATE())
            AND YEAR(created_at) = YEAR(CURRENT_DATE()) 
            ORDER BY created_at DESC
        ");

        echo '<h2>Liste des nouveaux '.sizeof($myrows).' joueurs</h2>';
        echo '<table>';
        echo '<tr><th>creation</th><th colspan="2">Joueur</th><th>balance</th></tr>';
        foreach($myrows as $pos => $player){
            echo '<tr><td width="100px">'.$player->created_at.'</td><td width="50px"><img src="https://minotar.net/bust/'.$player->username.'.png" width="50px"/></td><td>'.$player->username.'</td><td>'.$player->balance.' pièces</td></tr>';
        }
        echo '</table>';


    } catch (Exception $e) {
        error_log($e);
    }

}

add_action('admin_menu', 'test_plugin_setup_menu');

register_activation_hook( PLUGIN_WITH_CLASSES__FILE__, 'create_plugin_database_table');
add_shortcode( 'republicraft-votes', 'votes_shortcode' );
