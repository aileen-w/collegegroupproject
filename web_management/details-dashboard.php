<?php

    /**
     * Include required functions
     */
    include_once ("config/functions.php");


    // open PDO connection
    $pdo = conn();

    // authenticate user
    auth($pdo);

    $error = false;
    $section = "";
    if(!empty($_GET) && !empty($_GET['el']))
    {
        $data = details_page($pdo, $_GET['el']);
        $section = ucfirst(strtolower($_GET['el']));
    }
    else
    {
        $error = true;
    }

    /**
     * Include page head
     */
    include_once ("config/head.php");

?>

<!-- Page main content -->
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Details</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">

        <?php
            if($error)
            {
               echo '<div class="alert alert-danger" id="error" role="alert">Error occurred while fetching data.</div>';
            }
        ?>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title"><?=$section;?></h3>
            </div>
            <div class="panel-body">

                <?php
                    if(count($data)>0)
                    {
                        $keys = (array_keys($data[0]));
                        echo '<table class="table">';
                        echo '<thead>';
                        echo '<tr>';
                        foreach($keys as $column)
                        {
                            echo '<th>' . $column . '</th>';
                        }
                        echo '</tr>';
                        echo '</thead>';
                        echo '<tbody>';

                        foreach($data as $row)
                        {
                            echo '<tr>';
                            foreach($keys as $key)
                            {
                                echo '<td>' . $row[$key] . '</td>';
                            }
                            echo '</tr>';
                        }

                        echo '</tbody>';
                        echo '</table>';
                    }
                ?>

            </div>
        </div>
    <!-- /.row -->
</div>
<!-- /#page-wrapper -->


<?php
/**
 * Include page footer
 */
include_once ("config/footer.php");

// refresh page evry 5 seconds in order to show latest notifications
if($section == "Notifications")
{
    echo '<script type="text/javascript">
            setTimeout(function () { location.reload(true); }, 5000);
          </script>';
}
?>
