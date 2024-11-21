// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GameOver : MonoBehaviour
{
    // Define variables
    private Vector3 gameRunningPosition;
    private Vector3 gameOverPosition;

    // Define level manager
    [SerializeField] private LevelManager levelManager;

    private void Start()
    {
        // Initialise variables
        gameOverPosition = transform.position;
        gameRunningPosition = transform.position;
        gameRunningPosition.y = -2000;
    }

    private void Update()
    {
        // When level resets, bring up game over screen
        if (levelManager.ResetLevel)
        {
            transform.position = gameOverPosition;
        }
        // While game is running, do not show game over screen
        else
        {
            transform.position = gameRunningPosition;
        }
    }
}
