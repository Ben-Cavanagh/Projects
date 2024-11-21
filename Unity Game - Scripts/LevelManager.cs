// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelManager : MonoBehaviour
{
    // Define border variables
    [SerializeField] private float borderUp = 10f;
    [SerializeField] private float borderDown = -11f;
    [SerializeField] private float borderLeft = -18f;
    [SerializeField] private float borderRight = 18f;

    // Define score variables
    private int currentScore = 0;
    private int highScore = 0;

    // Define variables for the timer
    [Range(10,180)] [SerializeField] private float gameTimer = 120f;
    private float resetTimer;
    private int intTimer;

    // Define variable for resetting the level
    private bool resetLevel = false;

    private void Start()
    {
        // Set value for reset timer
        resetTimer = gameTimer;
    }

    private void Update()
    {
        if (resetLevel == false)
        {
            // Update value of timer
            gameTimer -= Time.deltaTime;
            intTimer = (int) gameTimer;

            // Update highscore if player gets a new high score
            if (currentScore > highScore)
            {
                highScore = currentScore;
            }

            // Reset level when timer is zero
            if (gameTimer <= 0f)
            {
                resetLevel = true;
                gameTimer = resetTimer;
                currentScore = 0;
            }
        }
    }

    // Method called by reset button in UI manager
    public void Reset()
    {
        resetLevel = false;
    }

    // Getter for ResetLevel
    public bool ResetLevel
    {
        get
        {
            return resetLevel;
        }
    }

    // Getter and setter for CurrentScore
    public int CurrentScore
    {
        get 
        {
            return currentScore;
        }
        set 
        {
            currentScore = value;
        }
    }

    // Getter for HighScore
    public int HighScore
    {
        get 
        {
            return highScore;
        }
    }

    // Getter for Timer
    public int Timer
    {
        get 
        {
            return intTimer;
        }
    }

    // Getter for all borders
    public float[] Borders
    {
        get
        {
            float[] borders = new float[4];
            borders[0] = borderUp;
            borders[1] = borderDown;
            borders[2] = borderLeft;
            borders[3] = borderRight;
            return borders;
        }
    }
}
